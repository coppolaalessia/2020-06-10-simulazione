package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private List <String> genres;
	private Graph <Actor, DefaultWeightedEdge> grafo;
	private Map <Integer, Actor> idMap; 
	
	
	public Model() {
		dao = new ImdbDAO();
		genres = new ArrayList<String>();
		idMap = new HashMap <Integer,Actor>();
		riempiMappa();
		
	}
	
	public void riempiMappa() {
		for(Actor a : this.dao.listAllActors()) {
			idMap.put(a.getId(),a);
		}
		
	}
	
	public List<String> getGenres(){
		if(genres.isEmpty()) {
			this.dao.getGenres(genres);
		}
		return genres;
	}
	
	public void creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(genere));
		
		//aggiungo archi
		for(Collegate c: this.dao.getArchi(genere, idMap)) {
			Graphs.addEdge(this.grafo, c.getA1(), c.getA2(), c.getPeso());
		}
		
	}

	public Integer getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Set<Actor> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public List<Actor> getAttoriSimili (Actor attore){
		List<Actor> simili = new ArrayList <Actor>();
		DepthFirstIterator<Actor, DefaultWeightedEdge> it = new DepthFirstIterator<Actor, DefaultWeightedEdge>(this.grafo, attore);
		
		while(it.hasNext()) {
			simili.add(attore)
		}
		Collections.sort(simili);
		return simili;
	}
}
