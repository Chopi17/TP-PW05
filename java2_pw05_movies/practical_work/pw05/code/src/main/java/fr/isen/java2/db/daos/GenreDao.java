package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


import fr.isen.java2.db.entities.Genre;

public class GenreDao {
	DataSource dataSource = DataSourceFactory.getDataSource();
	public List<Genre> listGenres() {
		List<Genre> listofgenre= new ArrayList<>();
		try(Connection connection = dataSource.getConnection()){
			try (Statement statement = connection.createStatement()){
				String sqlQuery="SELECT * FROM genre";
				try (ResultSet resultSet = statement.executeQuery(sqlQuery);){
					while( resultSet.next()) {
						Genre genre = new Genre(resultSet.getInt("idgenre"), resultSet.getString("name"));
						listofgenre.add(genre);
						}
					}
				statement.close();
				}
			connection.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		return listofgenre;
	}

	public Genre getGenre( String genre_name) {
		try (Connection connection = dataSource.getConnection()){
			String sqlQuery="SELECT * FROM genre WHERE 	name =?";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
				statement.setString(1, genre_name);
				try (ResultSet resultSet = statement.executeQuery()){
					if (resultSet.next()) {
						Genre genre = new Genre( resultSet.getInt("idgenre"), resultSet.getString("name"));
						statement.close();
						connection.close();
						return genre;
					}
				}
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Genre addGenre(String genre_name) {
		try (Connection connection = dataSource.getConnection()){
			String sqlQuery = "INSERT INTO genre(name) VALUES (?) ";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, genre_name);
				statement.executeUpdate();
				ResultSet ids= statement.getGeneratedKeys();
				if (ids.next()) {
					Genre genre = new Genre(ids.getInt(1), genre_name);
					statement.close();
					connection.close();
					return genre;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
}

