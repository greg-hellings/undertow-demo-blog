package com.thehellings.demo;

import com.thehellings.demo.db.Post;
import com.thehellings.gully.PlainRouter;
import com.thehellings.gully.http.ResponseCode;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.Headers;
import io.undertow.util.RedirectBuilder;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

public class AdminPostEdit implements HttpHandler {
	@Override
	public void handleRequest(HttpServerExchange httpServerExchange) {
		final PlainRouter.RouteAttachment pathArgs = httpServerExchange.getAttachment(PlainRouter.ATTACHMENT_KEY);
		int postId = 0;
		try {
			postId = Integer.parseInt(pathArgs.getParameters().get("postid"));
		} catch(NumberFormatException ex) {
			httpServerExchange.setResponseCode(ResponseCode.ERROR_NOT_FOUND.getCode());
			httpServerExchange.getResponseSender().send("Invalid post id");
			return;
		}
		ObjectContext objectContext = Main.getRuntime().newContext();
		Post post = ObjectSelect.query(Post.class).where(Post.ID.eq(postId)).selectFirst(objectContext);
		if (post == null) {
			httpServerExchange.setResponseCode(ResponseCode.ERROR_NOT_FOUND.getCode());
			httpServerExchange.getResponseSender().send("Post not found.");
		}
		FormData formData = httpServerExchange.getAttachment(FormDataParser.FORM_DATA);
		post.setTitle(formData.getFirst("title").getValue());
		post.setSummary(formData.getFirst("summary").getValue());
		post.setBody(formData.getFirst("body").getValue());
		objectContext.commitChanges();
		httpServerExchange.setResponseCode(ResponseCode.REDIRECT_PERMANENT.getCode());
		httpServerExchange.getResponseHeaders().add(Headers.LOCATION, RedirectBuilder.redirect(httpServerExchange, "/admin/post"));
	}
}
