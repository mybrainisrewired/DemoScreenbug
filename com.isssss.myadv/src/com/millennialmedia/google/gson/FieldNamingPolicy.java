package com.millennialmedia.google.gson;

import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy {
    IDENTITY {
        public String translateName(Field f) {
            return f.getName();
        }
    },
    UPPER_CAMEL_CASE {
        public String translateName(Field f) {
            return FieldNamingPolicy.access$100(f.getName());
        }
    },
    UPPER_CAMEL_CASE_WITH_SPACES {
        public String translateName(Field f) {
            return FieldNamingPolicy.access$100(FieldNamingPolicy.access$200(f.getName(), " "));
        }
    },
    LOWER_CASE_WITH_UNDERSCORES {
        public String translateName(Field f) {
            return FieldNamingPolicy.access$200(f.getName(), "_").toLowerCase();
        }
    },
    LOWER_CASE_WITH_DASHES {
        public String translateName(Field f) {
            return FieldNamingPolicy.access$200(f.getName(), "-").toLowerCase();
        }
    };

    private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
        return indexOfSubstring < srcString.length() ? firstCharacter + srcString.substring(indexOfSubstring) : String.valueOf(firstCharacter);
    }

    private static String separateCamelCase(String name, String separator) {
        StringBuilder translation = new StringBuilder();
        int i = 0;
        while (i < name.length()) {
            char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
            i++;
        }
        return translation.toString();
    }

    private static String upperCaseFirstLetter(String name) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        int index = 0;
        char firstCharacter = name.charAt(0);
        while (index < name.length() - 1 && !Character.isLetter(firstCharacter)) {
            fieldNameBuilder.append(firstCharacter);
            index++;
            firstCharacter = name.charAt(index);
        }
        if (index == name.length()) {
            return fieldNameBuilder.toString();
        }
        return !Character.isUpperCase(firstCharacter) ? fieldNameBuilder.append(modifyString(Character.toUpperCase(firstCharacter), name, index + 1)).toString() : name;
    }
}