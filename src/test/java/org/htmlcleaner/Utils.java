/*  Copyright (c) 2006-2007, Vladimir Nikic
    All rights reserved.

    Redistribution and use of this software in source and binary forms,
    with or without modification, are permitted provided that the following
    conditions are met:

    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the
      following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the
      following disclaimer in the documentation and/or other
      materials provided with the distribution.

    * The name of HtmlCleaner may not be used to endorse or promote
      products derived from this software without specific prior
      written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

    You can contact Vladimir Nikic by sending e-mail to
    nikic_vladimir@yahoo.com. Please include the word "HtmlCleaner" in the
    subject line.
*/

package org.htmlcleaner;

import java.io.*;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Common utilities.</p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public class Utils {

    /**
     * Reads content from the specified URL with specified charset into string
     * @param url
     * @param charset
     * @throws IOException
     */
    @Deprecated // Removing network I/O will make htmlcleaner better suited to a server environment which needs managed connections
    static CharSequence readUrl(URL url, String charset) throws IOException {
        StringBuilder buffer = new StringBuilder(1024);
        InputStream inputStream = url.openStream();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, charset);
            char[] charArray = new char[1024];

            int charsRead = 0;
            do {
                charsRead = reader.read(charArray);
                if (charsRead >= 0) {
                    buffer.append(charArray, 0, charsRead);
                }
            } while (charsRead > 0);
        } finally {
            inputStream.close();
        }

        return buffer;
    }

    /**
     * Escapes XML string.
     * @param s String to be escaped
     * @param props Cleaner properties affects escaping behaviour
     * @param isDomCreation Tells if escaped content will be part of the DOM
     */
    public static String escapeXml(String s, CleanerProperties props, boolean isDomCreation) {
        boolean advanced = props.isAdvancedXmlEscape();
        boolean recognizeUnicodeChars = props.isRecognizeUnicodeChars();
        boolean translateSpecialEntities = props.isTranslateSpecialEntities();
        boolean transResCharsToNCR = props.isTransResCharsToNCR();
        boolean transSpecialEntitiesToNCR = props.isTransSpecialEntitiesToNCR();
        return escapeXml(s, advanced, recognizeUnicodeChars, translateSpecialEntities, isDomCreation, transResCharsToNCR, transSpecialEntitiesToNCR);
    }
    /**
     * change notes:
     * 1) convert ascii characters encoded using &#xx; format to the ascii characters -- may be an attempt to slip in malicious html
     * 2) convert &#xxx; format characters to &quot; style representation if available for the character.
     * 3) convert html special entities to xml &#xxx; when outputing in xml
     * @param s
     * @param advanced
     * @param recognizeUnicodeChars
     * @param translateSpecialEntities
     * @param isDomCreation
     * @return
     * TODO Consider moving to CleanerProperties since a long list of params is misleading.
     */
    public static String escapeXml(String s, boolean advanced, boolean recognizeUnicodeChars, boolean translateSpecialEntities, 
                                   boolean isDomCreation, boolean transResCharsToNCR, boolean translateSpecialEntitiesToNCR) {
        if (s != null) {
    		int len = s.length();
    		StringBuilder result = new StringBuilder(len);

    		for (int i = 0; i < len; i++) {
    			char ch = s.charAt(i);

    			SpecialEntity code;
    			if (ch == '&') {
    				if ( (advanced || recognizeUnicodeChars) && (i < len-1) && (s.charAt(i+1) == '#') ) {
    					i = convertToUnicode(s, isDomCreation, recognizeUnicodeChars, translateSpecialEntitiesToNCR, result, i+2);
    				} else if ((translateSpecialEntities || advanced) &&
				        (code = SpecialEntities.INSTANCE.getSpecialEntity(s.substring(i, i+Math.min(10, len-i)))) != null) {
			            if (translateSpecialEntities && code.isHtmlSpecialEntity()) {
                            if (recognizeUnicodeChars) {
                                result.append( (char)code.intValue() );
                            } else {
                                result.append( code.getDecimalNCR() );
                            }
							i += code.getKey().length() + 1;
						} else if (advanced ) {
					        result.append(transResCharsToNCR ? code.getDecimalNCR() : code.getEscaped(isDomCreation));
		                    i += code.getKey().length()+1;
			            } else {
			                result.append(transResCharsToNCR ? getAmpNcr() : "&amp;");
			            }
    				} else {
    				    result.append(transResCharsToNCR ? getAmpNcr() : "&amp;");
    				}
    			} else if ((code = SpecialEntities.INSTANCE.getSpecialEntityByUnicode(ch)) != null ) {
    			    result.append(transResCharsToNCR ? code.getDecimalNCR() : code.getEscaped(isDomCreation));
    			} else {
    				result.append(ch);
    			}
    		}

    		return result.toString();
    	}

    	return null;
    }

    private static String ampNcr;

    private static String getAmpNcr() {
        if (ampNcr == null) {
            ampNcr = SpecialEntities.INSTANCE.getSpecialEntityByUnicode('&').getDecimalNCR();
        }

        return ampNcr;
    }

    private static final Pattern ASCII_CHAR = Pattern.compile("\\p{Print}");
    /**
     * @param s
     * @param domCreation
     * @param recognizeUnicodeChars
     * @param translateSpecialEntitiesToNCR 
     * @param result
     * @param i
     * @return
     */
    private static int convertToUnicode(String s, boolean domCreation, boolean recognizeUnicodeChars, boolean translateSpecialEntitiesToNCR, StringBuilder result, int i) {
        StringBuilder unicode = new StringBuilder();
        int charIndex = extractCharCode(s, i, true, unicode);
        if (unicode.length() > 0) {
        	try {
        	    boolean isHex = unicode.substring(0,1).equals("x");
        		char unicodeChar = isHex ?
                                        (char)Integer.parseInt(unicode.substring(1), 16) :
                                        (char)Integer.parseInt(unicode.toString());
                SpecialEntity specialEntity = SpecialEntities.INSTANCE.getSpecialEntityByUnicode(unicodeChar);
                if (unicodeChar == 0) {
                    // null character &#0Peanut for example
                    // just consume character &
                    result.append("&amp;");
                } else if ( specialEntity != null &&
                        // special characters that are always escaped.
                        (!specialEntity.isHtmlSpecialEntity()
                                // OR we are not outputting unicode characters as the characters ( they are staying escaped )
                                || !recognizeUnicodeChars)) {
                    result.append(domCreation? specialEntity.getHtmlString():
                        (translateSpecialEntitiesToNCR? (isHex? specialEntity.getHexNCR(): specialEntity.getDecimalNCR()) : 
                            specialEntity.getEscapedXmlString()));
                } else if ( recognizeUnicodeChars ) {
                    // output unicode characters as their actual byte code with the exception of characters that have special xml meaning.
                    result.append( String.valueOf(unicodeChar));
                } else if ( ASCII_CHAR.matcher(new String(new char[] { unicodeChar } )).find()) {
                    // ascii printable character. this fancy escaping might be an attempt to slip in dangerous characters (i.e. spelling out <script> )
                    // by converting to printable characters we can more easily detect such attacks.
                    result.append(String.valueOf(unicodeChar));
                } else {
        			result.append( "&#").append(unicode).append(";" );
        		}
        	} catch (NumberFormatException e) {
        	    // should never happen now
        		result.append("&amp;#").append(unicode).append(";" );
        	}
        } else {
        	result.append("&amp;");
        }
        return charIndex;
    }

    // TODO have pattern consume leading 0's and discard.
    public static Pattern HEX_STRICT = Pattern.compile("^([x|X][\\p{XDigit}]+)(;?)");
    public static Pattern HEX_RELAXED = Pattern.compile("^0*([x|X][\\p{XDigit}]+)(;?)");
    public static Pattern DECIMAL = Pattern.compile("^([\\p{Digit}]+)(;?)");
    /**
     * <ul>
     * <li>(earlier code was failing on this) - &#138A; is converted by FF to 3 characters: &#138; + 'A' + ';'</li>
     * <li>&#0x138A; is converted by FF to 6? 7? characters: &#0 'x'+'1'+'3'+ '8' + 'A' + ';'
     * #0 is displayed kind of weird</li>
     * <li>&#x138A; is a single character</li>
     * </ul>
     *
     * @param s
     * @param charIndex
     * @param relaxedUnicode '&#0x138;' is treated like '&#x138;'
     * @param unicode
     * @return the index to continue scanning the source string -1 so normal loop incrementing skips the ';'
     */
    private static int extractCharCode(String s, int charIndex, boolean relaxedUnicode, StringBuilder unicode) {
        int len = s.length();
        CharSequence subSequence = s.subSequence(charIndex, Math.min(len,charIndex+15));
        Matcher matcher;
        if( relaxedUnicode ) {
            matcher = HEX_RELAXED.matcher(subSequence);
        } else {
            matcher = HEX_STRICT.matcher(subSequence);
        }
        // silly note: remember calling find() twice finds second match :-)
        if (matcher.find() || ((matcher = DECIMAL.matcher(subSequence)).find())) {
            // -1 so normal loop incrementing skips the ';'
            charIndex += matcher.end() -1;
            unicode.append(matcher.group(1));
        }
        return charIndex;
    }


    /**
     * Checks if specified character can be part of xml identifier (tag name of attribute name)
     * and is not standard identifier character.
     * @param ch Character to be checked
     * @return True if it can be part of xml identifier
     */
    public static boolean isIdentifierHelperChar(char ch) {
        return ':' == ch || '.' == ch || '-' == ch || '_' == ch;
    }

    /**
     * Checks whether specified string can be valid tag name or attribute name in xml.
     * @param s String to be checked
     * @return True if string is valid xml identifier, false otherwise
     */
    public static boolean isValidXmlIdentifier(String s) {
        if (s != null) {
            int len = s.length();
            if (len == 0) {
                return false;
            }
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                if ( (i == 0 && !Character.isUnicodeIdentifierStart(ch)) ||
                     (!Character.isUnicodeIdentifierStart(ch) && !Character.isDigit(ch) && !Utils.isIdentifierHelperChar(ch)) ) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * @param o
     * @return True if specified string is null of contains only whitespace characters
     */
    public static boolean isEmptyString(Object o) {
        if ( o == null ) {
            return true;
        }
        String s = o.toString();
        String text = escapeXml(s, true, false, false, false, false, false);
        // TODO: doesn't escapeXml handle this?
        String last = text.replace(SpecialEntities.NON_BREAKABLE_SPACE, ' ').trim();
        return last.length() == 0;
    }

    public static String[] tokenize(String s, String delimiters) {
        if (s == null) {
            return new String[] {};
        }

        StringTokenizer tokenizer = new StringTokenizer(s, delimiters);
        String result[] = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            result[index++] = tokenizer.nextToken();
        }

        return result;
    }

    /**
     * @param name
     * @return For xml element name or attribute name returns prefix (part before :) or null if there is no prefix
     */
    public static String getXmlNSPrefix(String name) {
        int colIndex = name.indexOf(':');
        if (colIndex > 0) {
            return name.substring(0, colIndex);
        }

        return null;
    }

    /**
     * @param name
     * @return For xml element name or attribute name returns name after prefix (part after :)
     */
    public static String getXmlName(String name) {
        int colIndex = name.indexOf(':');
        if (colIndex > 0 && colIndex < name.length() - 1) {
            return name.substring(colIndex + 1);
        }

        return name;
    }
    
    static boolean isValidInt(String s, int radix) {
        try {
            Integer.parseInt(s, radix);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    static boolean isValidXmlChar(char ch) {
        return ((ch >= 0x20) && (ch <= 0xD7FF)) ||
               (ch == 0x9) ||
               (ch == 0xA) ||
               (ch == 0xD) ||
               ((ch >= 0xE000) && (ch <= 0xFFFD)) ||
               ((ch >= 0x10000) && (ch <= 0x10FFFF));
    }
    
}