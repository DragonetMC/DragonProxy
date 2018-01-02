/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PatternChecker {

    /*
     * TODO "Beware the regex e-mail validation, for this way lies madness." -egg82
     */

    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);

    public PatternChecker() {

    }

    public static boolean matchEmail(String email) {
        Matcher matcher = PATTERN_EMAIL.matcher(email);
        return matcher.matches();
    }
}
