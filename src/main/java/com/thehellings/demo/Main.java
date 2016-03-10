package com.thehellings.demo;

import com.thehellings.demo.db.Post;
import com.thehellings.demo.db.User;
import com.thehellings.gully.Server;
import com.thehellings.gully.config.DefaultEnvironment;
import io.undertow.Undertow;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.configuration.DefaultRuntimeProperties;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.log.CommonsJdbcEventLogger;
import org.apache.cayenne.log.JdbcEventLogger;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.query.NamedQuery;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.query.SelectQuery;
import org.jamon.TemplateManager;
import org.jamon.TemplateManagerSource;
import org.jamon.compiler.RecompilingTemplateManager;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;

public class Main {
    private static ServerRuntime runtime = new ServerRuntime("db/cayenne-blog.xml");
	private static final Logger log = Logger.getLogger(Main.class);

    /**
     * Get this instance of the Cayenne {@link ServerRuntime}, used as the base of all Cayenne operations
     * throughout the application.
     *
     * @return the Caynne ServerRuntime instance
     */
    public static ServerRuntime getRuntime() {
        return runtime;
    }

    public static void main(String[] args) throws Exception {
        if (Options.instance().getEnvironment() == Options.Environment.DEVELOPMENT) {
            // When we're in development we want to live-reload the templates
            TemplateManager manager = new RecompilingTemplateManager(new RecompilingTemplateManager.Data().setSourceDir("src/main/templates"));
            TemplateManagerSource.setTemplateManager(manager);
            // We also want to generate the database schema, if possible
            DataDomain domain = runtime.getDataDomain();
            DataMap map = runtime.getDataDomain().getDataMap("datamap");
            JdbcEventLogger logger = new CommonsJdbcEventLogger(new DefaultRuntimeProperties(new HashMap<String, String>()));
            DbGenerator generator = new DbGenerator(domain.getDataNode("memory").getAdapter(), map, logger);
            // Enable these two if you're not using an in-memory but you still want old stuff blown away
            generator.setShouldDropTables(true);
            generator.setShouldDropPKSupport(true);
            generator.runGenerator(domain.getDataNode("memory").getDataSource());
            // And now insert some random data
            ObjectContext context = getRuntime().newContext();
            // Admin user
            User user = context.newObject(User.class);
            user.setUsername("admin");
            user.setPassword("Admin");
            user.setIsAdmin(true);
            Post post = context.newObject(Post.class);
            post.setTitle("Title 1");
            post.setSummary("This is a <sup>weird</sup> summary");
            post.setBody("A very strange and weird thing happened on the way to the theater the other day.");
            post = context.newObject(Post.class);
            post.setTitle("Older Posts Come First");
            post.setSummary("Another Strage & Weird Thing");
            post.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut ipsum lectus, placerat eu orci et, ultricies tincidunt tortor. Integer dictum faucibus sapien, eget auctor leo. Pellentesque eleifend ac est vitae mattis. Quisque odio felis, ultricies ac sollicitudin non, maximus eget sapien. Ut vitae finibus nulla, non dignissim risus. Sed interdum urna leo. Nunc in porttitor velit. Vestibulum vestibulum, nisl sed tristique ultrices, sem arcu gravida ligula, eu ullamcorper nisl metus pulvinar turpis. Vestibulum aliquet leo dictum mattis ultricies. Sed ut nunc consequat, iaculis urna dapibus, vulputate lectus. Ut quis elementum dui. Ut consectetur dapibus quam, sed mattis purus tristique et. Aliquam erat volutpat. Vivamus gravida elit id neque porttitor tempor.\n" +
                    "\n<p>" +
                    "Aenean venenatis eleifend laoreet. Etiam congue arcu ac ex volutpat, non interdum eros accumsan. Cras vel efficitur turpis, congue tristique augue. Integer bibendum commodo diam, et tristique leo pharetra in. Proin sodales laoreet turpis. Vestibulum bibendum nisi et velit convallis aliquam. Praesent tincidunt malesuada tincidunt. Nullam consectetur ut neque lobortis suscipit. Vivamus dignissim tellus a velit vulputate, id consectetur nisi aliquam. Cras et velit eget lorem vulputate mattis. Cras posuere efficitur nulla. Cras sed rhoncus ipsum. Curabitur posuere faucibus orci, non aliquet tortor sodales dignissim. Aliquam sed massa dapibus, finibus magna sit amet, sollicitudin mi. Vestibulum justo nisi, pharetra a congue vitae, viverra ac nunc. Suspendisse quis lacus non leo placerat sollicitudin quis sit amet justo.\n" +
                    "\n" +
                    "Vestibulum eget ullamcorper sem, sed pretium dui. Duis finibus enim eu dolor scelerisque, eu mattis mi mattis. Pellentesque pulvinar imperdiet ex sit amet pretium. Mauris scelerisque vitae odio sit amet ultricies. Donec pellentesque diam non suscipit porta. Duis vestibulum arcu magna, in euismod nunc congue vel. Maecenas pellentesque euismod justo vitae vehicula. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;\n" +
                    "\n<p>" +
                    "Ut sit amet tellus mattis nisl viverra congue eget nec libero. Vivamus vel euismod elit. Duis quis nisi ultrices, sodales nisl et, tincidunt lectus. Ut arcu tellus, convallis id tincidunt ac, hendrerit non mauris. In hac habitasse platea dictumst. Fusce vulputate ante id suscipit mattis. Sed vel fringilla enim, rhoncus efficitur ligula. Nam tincidunt eleifend ipsum a malesuada. Integer lobortis augue nec sapien eleifend condimentum. Nullam vestibulum venenatis felis at sagittis. Etiam ex leo, gravida eu ligula vel, condimentum iaculis neque. Donec lacinia arcu vel consequat aliquet. Mauris sed tincidunt libero.\n" +
                    "\n" +
                    "Nunc pretium rutrum leo, ut posuere elit accumsan eu. In nec scelerisque nunc. Nullam vitae ex neque. Nam viverra luctus ligula id pretium. Maecenas ullamcorper ligula orci, sed lobortis leo gravida et. Quisque nec gravida libero. Curabitur egestas auctor nisi, maximus aliquet tellus laoreet non. Cras non nisl eros. Phasellus eu maximus augue. Vivamus eget maximus sem, ac vulputate est. Aenean eget leo in magna efficitur molestie ut ac orci. Suspendisse vitae metus nulla. Curabitur sit amet dui finibus augue imperdiet euismod at sed justo. Aliquam eleifend quam non arcu tincidunt scelerisque. Aenean euismod faucibus vehicula.");
            context.commitChanges();
        }
        Server server = new Server(new DefaultEnvironment("Yay"), new Router());
	    server.start();
        runtime.shutdown();
    }

	public static List<Post> getPosts() {
		SelectQuery query = new SelectQuery(Post.class);
		query.setFetchLimit(5);
		List<Post> posts = Main.getRuntime().newContext().performQuery(query);
		return posts;
	}

	public static int getPostCount() {
		List<DataRow> rows = Main.getRuntime().newContext().performQuery(new NamedQuery("Post count"));
		return ((Long)rows.get(0).get("COUNT")).intValue();
	}
}
