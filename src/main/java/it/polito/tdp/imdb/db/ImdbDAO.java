package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Collegate;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getGenres(List<String> result){
		String sql ="SELECT distinct genre "
				+ "FROM movies_genres "
				+ "ORDER BY genre";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String genre = new String(res.getString("genre"));
				result.add(genre);
			}
			conn.close();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			}
		
	}
	
	public List<Actor> getVertici (String genere){
		String sql= "SELECT a.id, a.first_name, a.last_name, a.gender "
				+ "FROM actors a, (SELECT r.actor_id "
				+ "	FROM movies_genres m, roles r "
				+ "	WHERE m.movie_id = r.movie_id and m.genre = ? "
				+ "	Group BY r.actor_id) AS t "
				+ "WHERE a.id= t.actor_id";
		
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("a.id"), res.getString("a.first_name"), res.getString("a.last_name"),
						res.getString("a.gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Collegate> getArchi (String genere, Map <Integer, Actor> idMap){
		String sql ="SELECT r1.actor_id, r2.actor_id, COUNT(*) AS peso "
				+ "FROM roles r1, roles r2, movies_genres m "
				+ "WHERE r1.actor_id > r2.actor_id AND r1.movie_id= r2.movie_id AND r1.movie_id = m.movie_id AND m.genre= ? "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		
		List<Collegate> result = new ArrayList<Collegate>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Collegate collegato =
						new Collegate(idMap.get(res.getInt("r1.actor_id")),idMap.get(res.getInt("r2.actor_id")), res.getInt("peso"));
				
				result.add(collegato);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
