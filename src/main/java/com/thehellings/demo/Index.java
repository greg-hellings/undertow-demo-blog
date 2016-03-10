package com.thehellings.demo;

import com.thehellings.demo.db.Post;
import com.thehellings.demo.templates.IndexTemplate;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.cayenne.query.SelectQuery;

import java.util.List;

public class Index implements HttpHandler
{
	public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
		SelectQuery query = new SelectQuery(Post.class);
		query.setFetchLimit(5);
		List<Post> posts = Main.getRuntime().newContext().performQuery(query);
		httpServerExchange.getResponseSender().send(new IndexTemplate().makeRenderer(posts).asString());
	}
}