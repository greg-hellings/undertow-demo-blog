package com.thehellings.demo;

import java.io.File;

/**
 * A singleton class that permits the setting and handling of various options for the
 * site application. This base implementation provides only a small subset of very
 * weakly specified default values. You should extend it and inject the values you want
 * into the classes where they are needed.
 */
public class Options {
    /**
     * A list of environments.
     *
     * Certain conditions in the code should be operated on in different ways
     * if we are in production or development environments. Use these values to
     * switch on which value to leverage.
     */
    public enum Environment {
        PRODUCTION,
        DEVELOPMENT
    }

    /**
     * Singleton instance
     */
    private static final Options self = new Options();

    /**
     * The current operating environment.
     *
     * Use this value to decide on things like whether to monitor templates for
     * changes, whether to cache static values, etc
     */
    private Environment environment;

    /**
     * The root folder where CSS files should be served from
     */
    private File cssRoot;

    /**
     * The root folder where JS files should be served from
     */
    private File jsRoot;

    /**
     * The root folder where fonts files should be served from
     */
    private File fontsRoot;

    /**
     * Singleton accessor
     *
     * Use this method to fetch back an instance of the system options as set
     * in the execution of this.
     *
     * @return The singleton instance of this class
     */
    public static Options instance() {
        return self;
    }

    /**
     * The current execution environment
     *
     * Indicates the current execution environmen in order to permit different behaviors
     * in different environments, such as hot-reloading of templates, monitoring of source
     * directories instead of build directories for resource changes, etc.
     *
     * @return The current execution environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * The proper CSS root directory to serve out of for this environment
     *
     * @return the proper CSS file root
     * @see Options#setCssRoot()
     */
    public File getCssRoot() {
        return this.cssRoot;
    }

    /**
     * The proper JS root directory to serve out of for this environment
     *
     * @return the proper JS file root
     * @see Options#setJsRoot()
     */
    public File getJsRoot() {
        return this.jsRoot;
    }

    /**
     * The proper Fonts root directory to serve out of for this environment
     *
     * @return the proper Fonts file root
     * @see Options#setFontsRoot()
     */
    public File getFontsRoot() {
        return this.fontsRoot;
    }

    /**
     * Allows the environment to be set, but not altered by other factors.
     *
     * Retain all logic within this class. If you wish to alter the logic used to determine the
     * current execution environment, extend this class and inject it into the runtime of your
     * classes.
     */
    private void setEnvironment() {
        this.environment = Environment.DEVELOPMENT;
    }

    /**
     * Sets the current root of the CSS folders.
     *
     * By default, this is src/main/resources/css if there is the current environment is Development
     * and is the "css" resource off the current ClassLoader in production environments. This allows
     * for live editing of the files while in the development cycle and eliminates the need for
     * a recompile and restart of the application every time a CSS resource is edited
     */
    private void setCssRoot() {
        if (getEnvironment() == Environment.DEVELOPMENT) {
            this.cssRoot = this.devFiles("src/main/resources/css");
        } else {
            this.cssRoot = this.prodFiles("js");
        }
    }

    /**
     * Sets the current root of the javascript folders.
     *
     * Operates very similarly to the {@link #setCssRoot()} method, but looks for the folder named "js"
     * instead.
     */
    private void setJsRoot() {
        if (getEnvironment() == Environment.DEVELOPMENT) {
            this.jsRoot = this.devFiles("src/main/resources/js");
        } else {
            this.jsRoot = this.prodFiles("js");
        }
    }

    /**
     * Sets the current root of the fonts folders.
     *
     * Operates very similarly to {@link #setCssRoot()} looking instead for a folder named "fonts".
     */
    private void setFontsRoot() {
        if (this.getEnvironment() == Environment.DEVELOPMENT) {
            this.fontsRoot = this.devFiles("src/main/resources/fonts");
        } else {
            this.fontsRoot = this.prodFiles("fonts");
        }
    }

    /**
     * Converts a relative path into an absolute path from the CWD. Useful for crafting the server
     * directories for the development resources.
     *
     * If you have static resources in src/main/resources/somefolder and you are executing a development
     * environment from the root of your source folder (e.g. with 'mvn exec:exec') then invoking this with
     * the argument "src/main/resources/somefolder" will return a suitable File object pointing to that
     * folder
     *
     * @param path relative path to convert to a File object
     * @return absolute File object for development environments
     */
    protected File devFiles(String path) {
        return new File(new File("").getAbsoluteFile(), path).getAbsoluteFile();
    }

    /**
     * Converts a folder name into the File object that pulls from the bundled application resources
     * for the current resource.
     *
     * If your source static resources for {@link #devFiles(String)} are copied to your artifact as most
     * resources will be coiped, you can fetch the appropriate File object by calling prodFiles("somefolder")
     * to serve the bundled results of that build process.
     *
     * @param path resource folder name
     * @return absolute File object for production environments
     */
    protected File prodFiles(String path) {
        return new File(this.getClass().getClassLoader().getResource(path).getPath());
    }
    /**
     * Private constructor to prevent there being a plethora of application options
     */
    private Options() {
        setEnvironment();
        setCssRoot();
        setJsRoot();
        setFontsRoot();
    }
}