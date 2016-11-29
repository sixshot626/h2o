/*
    Copyright 2008 Jenkov Development

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/



/**
 * User: Administrator
 */
package com.jenkov.db.impl.mapping;

import com.jenkov.db.impl.WordTokenizer;
import com.jenkov.db.itf.mapping.IDbNameGuesser;
import com.jenkov.db.util.ClassUtil;

import java.lang.reflect.Method;
import java.util.*;

public class DbNameGuesser implements IDbNameGuesser {

    public Collection getPossibleNames(String name){
        Set names = new HashSet();
        String fieldName = name;
        String fieldNameFirstLetterLowerCase =
                fieldName.substring(0,1).toLowerCase() + fieldName.substring(1, fieldName.length()) ;
        String fieldNameFirstLetterUpperCase =
                fieldName.substring(0,1).toUpperCase() + fieldName.substring(1, fieldName.length()) ;
        names.add(fieldName);
        names.add(fieldNameFirstLetterUpperCase);
        names.add(fieldNameFirstLetterLowerCase);
        names.add(fieldName.toUpperCase());
        names.add(fieldName.toLowerCase());

        List words = getWords(fieldName);

        String underScoreFieldName = getUnderScoreSeparatedWordsColumnName(words);
        names.add(underScoreFieldName);
        names.add(underScoreFieldName.toLowerCase());
        names.add(underScoreFieldName.toUpperCase());

        return names;
    }

    public Collection getPossibleColumnNames(Method member){
        if(ClassUtil.isGetter(member)){
            if(member.getName().startsWith("is")){
                return getPossibleNames(member.getName().substring(2, member.getName().length()));
            } else {
                return getPossibleNames(member.getName().substring(3, member.getName().length()));
            }
        } else if( ClassUtil.isSetter(member)){
            return getPossibleNames(member.getName().substring(3, member.getName().length()));
        }
        return getPossibleNames(member.getName());
    }

    public Collection getPossibleTableNames(Class objectClass) {
        Collection possibleNames = getPossibleNames(ClassUtil.classNameWithoutPackage(objectClass));
        Collection possibleNamesPlural = new ArrayList();

        Iterator iterator = possibleNames.iterator();
        while(iterator.hasNext()){
            String nameInSingular = (String) iterator.next();

            //create plural names for name guessing both lowercase and uppercase "s" in the end.
            if(isLastCharacterUpperCase(nameInSingular)){
                possibleNamesPlural.add(nameInSingular + "S");
                possibleNamesPlural.add(nameInSingular + "s");
            } else {
                possibleNamesPlural.add(nameInSingular + "s");
            }
        }
        possibleNames.addAll(possibleNamesPlural);
        return possibleNames;
    }

    private boolean isLastCharacterUpperCase(String nameInSingular) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ������";
        String lowerCase = "abcdefghijklmnopqrstuvwzyz������";
        String lastCharacter = nameInSingular.length() > 0 ?
                nameInSingular.substring(nameInSingular.length()-1, nameInSingular.length()) : "";
        if(upperCase.indexOf(lastCharacter) > -1 ){
            return true;
        }
        return false;
    }


    private String getUnderScoreSeparatedWordsColumnName(List words) {
        StringBuffer buffer = new StringBuffer();

        Iterator iterator = words.iterator();
        while(iterator.hasNext()){
            buffer.append(iterator.next());
            if(iterator.hasNext()){
                buffer.append('_');
            }
        }

        return buffer.toString();
    }


    protected List getWords(String fieldName){
        WordTokenizer tokenizer = new WordTokenizer(fieldName);
        List words = new ArrayList();

        while(tokenizer.hasMoreWords()){
            words.add(tokenizer.nextWord());
        }

        return words;
    }



}
