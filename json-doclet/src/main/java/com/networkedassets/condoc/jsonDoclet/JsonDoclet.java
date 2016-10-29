package com.networkedassets.condoc.jsonDoclet;

import com.networkedassets.condoc.jsonDoclet.model.Root;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

import java.util.logging.Logger;

public class JsonDoclet {

	private final static Logger log = Logger.getLogger(JsonDoclet.class.getName());

	/**
	 * The parsed object model.
	 */
	public static Root root;

	public static boolean start(RootDoc rootDoc) {
		Parser parser = new Parser();
		root = parser.parseRootDoc(rootDoc);
		return true;
	}

	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}
}
