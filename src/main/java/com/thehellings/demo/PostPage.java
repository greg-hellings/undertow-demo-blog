package com.thehellings.demo;

import com.thehellings.demo.db.Post;
import com.thehellings.demo.templates.PostPageTemplate;
import com.thehellings.gully.PlainRouter;
import com.thehellings.gully.http.ResponseCode;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.cayenne.query.ObjectSelect;

public class PostPage implements HttpHandler {
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        final PlainRouter.RouteAttachment attachment = exchange.getAttachment(PlainRouter.ATTACHMENT_KEY);
		int postId = 0;
		try {
			postId = Integer.parseInt(attachment.getParameters().get("postid"));
		} catch(NumberFormatException exception) {
			exchange.setResponseCode(ResponseCode.ERROR_NOT_FOUND.getCode());
			exchange.getResponseSender().send("Post not found.");
		}
		Post post = ObjectSelect.query(Post.class).where(Post.ID.eq(postId)).selectFirst(Main.getRuntime().newContext());
		exchange.getResponseSender().send(new PostPageTemplate().makeRenderer(post).asString());
    }
}
