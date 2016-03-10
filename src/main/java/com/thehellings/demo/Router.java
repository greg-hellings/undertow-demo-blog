package com.thehellings.demo;

import com.thehellings.demo.auth.BlogIdentityManager;
import com.thehellings.gully.PlainRouter;
import com.thehellings.gully.AuthenticatedRouter;
import com.thehellings.gully.http.Verb;
import com.thehellings.gully.resources.StaticHandler;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.server.handlers.form.EagerFormParsingHandler;

import java.util.Collections;

public class Router extends PlainRouter {
	private Options options;

    public Router () {
		this(Options.instance());
    }

    public Router(final Options options) {
        super();
		this.options = options;
        this.addRoute("/", new Index());
        this.addPrefixRoute("/css", new StaticHandler(this.options.getCssRoot()));
        this.addPrefixRoute("/js", new StaticHandler(this.options.getJsRoot()));
        this.addPrefixRoute("/fonts", new StaticHandler(this.options.getFontsRoot()));
        this.addParameterizedRoute("post/{postid}/{comment}", new PostPage());

		AuthenticatedRouter authenticatedRouter = new AuthenticatedRouter(Collections.singletonList(new BasicAuthenticationMechanism("THE REALM!")), new BlogIdentityManager(), new AdminPostListPage());
		authenticatedRouter.addExactRoute(Verb.GET, "/post", new AdminPostListPage(), true);
        authenticatedRouter.addParameterizedRoute(Verb.GET, "post/{postid}", new AdminPostPage());
	    authenticatedRouter.addParameterizedRoute(Verb.POST, "post/{postid}", new EagerFormParsingHandler().setNext(new AdminPostEdit()));
        this.addPrefixRoute("/admin", authenticatedRouter);
    }
}