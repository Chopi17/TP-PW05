package fr.isen.java2.db.daos;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {
	DataSource dataSource = DataSourceFactory.getDataSource();
	public List<Film> listFilms() {
		Genre genre =new Genre();
		List<Film> listoffilm= new ArrayList<>();
		try (Connection connection = dataSource.getConnection()){
			try (Statement statement = connection.createStatement()){
				try (ResultSet resultSet= statement.executeQuery("SELECT * from film JOIN genre ON film.genre_id = genre.idgenre")){
					while(resultSet.next()){
						genre.setName(resultSet.getString("name"));
						genre.setId(resultSet.getInt("idgenre"));
						Film film = new Film(resultSet.getInt("idfilm"),resultSet.getString("title"),resultSet.getDate("release_date").toLocalDate(),genre, resultSet.getInt("duration"), resultSet.getString("director"), resultSet.getString("summary"));
						listoffilm.add(film);
					}
					
				}
				statement.close();
			}
			connection.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listoffilm;
	}

	public List<Film> listFilmsByGenre(String genreName) {
		List<Film> listoffilm= new ArrayList<>();
		Genre genre = new Genre();
		try (Connection connection = dataSource.getConnection()){
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = ?")){
				// parameters statement
				statement.setString(1, genreName);
				try( ResultSet resultSet= statement.executeQuery()){
					while( resultSet.next()) {
						genre.setName(resultSet.getString("name"));
						genre.setId(resultSet.getInt("idgenre"));
						Film film = new Film(resultSet.getInt("idfilm"),resultSet.getString("title"),resultSet.getDate("release_date").toLocalDate(),genre, resultSet.getInt("duration"), resultSet.getString("director"), resultSet.getString("summary"));
						listoffilm.add(film);
					}
					
				}
				statement.close();
			}
			connection.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listoffilm;
	}

	public Film addFilm(Film film) {
		Film addFilm = new Film();
		Genre genre = new Genre();
		try (Connection connection = dataSource.getConnection()){
			String sqlQuery = "INSERT INTO film(title, release_date, genre_id, duration, directon, summary) VALUES(?,?,?,?,?,?)";
			// Creation of Statement
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
				statement.setString(1, film.getTitle());
				statement.setDate(2, Date.valueOf(film.getReleaseDate()));
				statement.setInt(3, film.getGenre().getId());
				statement.setInt(4, film.getDuration());
				statement.setString(5, film.getDirector());
				statement.setString(6, film.getSummary());
				// Requite
				int nbofRows = statement.executeUpdate();
				System.out.println(String.format("%d row(s) have been modified", nbofRows));
				statement.close();
			}
			//return variable 
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM film JOIN genre ON film.genre_id= genre.idgenre WHERE film.title= ?")){
				statement.setString(1,  film.getTitle());
				try (ResultSet resultSet= statement.executeQuery()){
					addFilm.setId(resultSet.getInt("idfilm"));
					addFilm.setTitle(resultSet.getString("title"));
					addFilm.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
					genre.setName(resultSet.getString("name"));
					genre.setId(resultSet.getInt("idgenre"));
					addFilm.setGenre(genre);
					addFilm.setDuration(resultSet.getInt("duration"));
					addFilm.setDirector(resultSet.getString("director"));
					addFilm.setSummary(resultSet.getString("summary"));
					}
				statement.close();
				}
		connection.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addFilm;
	}
}
