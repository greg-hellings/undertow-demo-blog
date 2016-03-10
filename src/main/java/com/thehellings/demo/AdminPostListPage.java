package com.thehellings.demo;

import com.thehellings.demo.db.Post;
import com.thehellings.demo.templates.AdminPostListTemplate;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.List;

public class AdminPostListPage implements HttpHandler {
	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		final List<Post> posts = Main.getPosts();
		AdminPostListTemplate template = new AdminPostListTemplate();
        exchange.getResponseSender().send(template.makeRenderer(posts).asString());
	}
}
