package h2o.common.util.green;

import h2o.apache.commons.lang.StringUtils;

import java.util.*;

public class SensitiveWordScanner {


    private final Map<Character, List<SensitiveWord>> firstWordMap;
    
    private final CharMapper charMapper;
    
    private final CharScanner charScanner;

    public SensitiveWordScanner( List<SensitiveWord>  sensitiveWords ) {
        this( sensitiveWords , LOWERCASE_CHAR_MAPPER , EQUALS_CHAR_SCANNER );
    }


    public SensitiveWordScanner( List<SensitiveWord>  sensitiveWords , CharMapper charMapper , CharScanner charScanner ) {

        this.charMapper = charMapper;
        this.charScanner = charScanner;

        Map<Character, List<SensitiveWord>> _firstWordMap = new HashMap<>();

        for ( SensitiveWord sensitiveWord : sensitiveWords ) {

            Character firstWord = charMapper.map( sensitiveWord.word.charAt(0) );

            List<SensitiveWord> words = _firstWordMap.computeIfAbsent(firstWord, k -> new ArrayList<>());
            words.add( sensitiveWord );
        }

        this.firstWordMap = _firstWordMap;

    }

    private SensitiveWordScanner(Map<Character, List<SensitiveWord>> firstWordMap, CharMapper charMapper, CharScanner charScanner) {
        this.firstWordMap = firstWordMap;
        this.charMapper = charMapper;
        this.charScanner = charScanner;
    }


    public SensitiveWordScanner charScanner( CharScanner charScanner ) {
        return new SensitiveWordScanner( this.firstWordMap , this.charMapper , charScanner );
    }



    public ScanResult scan(String text ) {

        if (StringUtils.isBlank( text )) {
            return ScanResult.PASS;
        }

        return scan( new Word(text,0) );

    }

    private ScanResult scan( Word text ) {

        for ( Word word = text ; ; word = word.moveNext() ) {

            ScanResult result = scan0(word);
            if ( result.hit ) {
                return result;
            }

            if (word.isEnd() ) {
                break;
            }

        }

        return ScanResult.PASS;

    }


    public String replace( String text ) {
        return this.replace( text , '*' );
    }

    public String replace( String text , char c ) {

        if (StringUtils.isBlank( text )) {
            return text;
        }

        Word word = new Word(text , 0);

        for (  ;; word = word.moveNext()) {

            ScanResult result = scan(word);
            if ( result.hit ) {
               word = new Word( replaceChar( word.str , result.indexes , c ) , result.indexes[0] );
            }


            if (word.isEnd() ) {
                break;
            }

        }

        return word.str;


    }


    private static String replaceChar( String text , int[] indexes , char c ) {

        char[] chars = text.toCharArray();
        for (int index : indexes) {
            chars[index] = c;
        }

        return new String( chars );
    }



    private ScanResult scan0( Word text ) {

        Character key = charMapper.map( text.currChar());

        List<SensitiveWord>  sensitiveWords = firstWordMap.get( key );
        if ( sensitiveWords == null || sensitiveWords.isEmpty() ) {
            return ScanResult.PASS;
        }

        for ( SensitiveWord sensitiveWord : sensitiveWords ) {
            ScanResult result = test( text , sensitiveWord );
            if ( result.hit ) {
                return result;
            }
        }

        return ScanResult.PASS;

    }





    private ScanResult test( Word text , SensitiveWord sensitiveWord ) {


        Indexes indexes = new Indexes( sensitiveWord.word.length() );
        indexes.add(text.position);

        if ( indexes.ok() ) {
            return ScanResult.find( sensitiveWord , indexes.value() );
        }


        for (int i = 1 , si = 1 , sl = 1 , len = text.length(); i < len; i++) {

            char testChar = charMapper.map( text.charAt(i) );
            char sensitiveChar = charMapper.map( sensitiveWord.charAt(si) );

            CharScanResult res = charScanner.scan(testChar, sensitiveChar, text.str, indexes, text.index(i), sensitiveWord, si);

            if ( res == CharScanResult.HIT ) {
                
                indexes.add( text.position + i );

                if ( indexes.ok() ) {
                    return ScanResult.find( sensitiveWord , indexes.value() );
                }

                si++;
                sl++;

            } else if ( res == CharScanResult.PASS ) {
                sl ++;
            }

            if ( sl + indexes.space() > sensitiveWord.scanLen ) {
                break;
            }


        }

        return ScanResult.PASS;

    }



    public static final CharMapper LOWERCASE_CHAR_MAPPER = c -> {
        if (c >= 'A' && c <= 'Z')
            return (char) (c | 0x20);
        return c;
    };


    public static final CharScanner EQUALS_CHAR_SCANNER = (testChar, sensitiveChar, text, indexes, ti, sw, si) ->
            testChar == sensitiveChar ? CharScanResult.HIT : CharScanResult.PASS;




    private static final class Word {

        public final String str;

        public final int position;

        public Word(String str, int position) {

            if ( str.length() <= position  ) {
                throw new IllegalArgumentException();
            }

            this.str = str;
            this.position = position;
        }


        public int length() {
            return str.length() - position;
        }

        public char currChar() {
            return str.charAt( this.position );
        }


        public int index( int idx ) {
            if (  idx >= this.length()  ) {
                throw new IllegalArgumentException();
            }
            return this.position + idx;
        }
        public char charAt( int idx ) {
            return str.charAt( this.index(idx) );
        }


        public Word moveTo( int idx ) {
            return new Word( this.str , idx );
        }

        public Word moveNext() {
            return new Word( this.str , position + 1 );
        }

        public boolean isEnd() {
            return this.position == str.length() - 1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Word{");
            sb.append("str='").append(str).append('\'');
            sb.append(", position=").append(position);
            sb.append('}');
            return sb.toString();
        }
    }





    public static final class  SensitiveWord {
        public final String word;

        public final int scanLen;


        public SensitiveWord( String word, int scanLen ) {

            if ( StringUtils.isBlank(word) ) {
                throw new IllegalArgumentException();
            }

            this.word    = word;
            this.scanLen = Math.max(scanLen, word.length());
        }

        public Character charAt( int idx ) {
            if (  idx >= word.length()  ) {
                throw new IllegalArgumentException();
            }

            return word.charAt(idx );
        }

        public boolean isEnd( int idx ) {
            return idx >= word.length() - 1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SensitiveWord{");
            sb.append("word='").append(word).append('\'');
            sb.append(", scanLen=").append(scanLen);
            sb.append('}');
            return sb.toString();
        }
    }


    public static final class Indexes {

        private final int[] idxs;

        private int i = 0;

        public Indexes( int n ) {
            this.idxs = new int[n];
        }

        public void add( int idx ) {
            if ( i >= idxs.length ) {
                throw new IllegalArgumentException();
            }
            idxs[i++] = idx;
        }

        public int get( int idx ) {

            if ( idx >= 0 ) {
                if (idx >= this.i) {
                    throw new IllegalArgumentException();
                }
                return idxs[idx];
            } else {
                int idx2 = i + idx;
                if (idx2 < 0) {
                    throw new IllegalArgumentException();
                }
                return idxs[idx2];
            }
        }


        public int space() {
            return idxs.length - i;
        }

        public boolean ok() {
            return space() == 0;
        }

        public int[] value() {

            if ( ok() ) {
                return idxs;
            }

            int[] r = new int[i];
            System.arraycopy( idxs , 0 , r , 0 , i );
            return r;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Indexes");
            sb.append(Arrays.toString(idxs));
            return sb.toString();
        }
    }


    public static final class ScanResult {

        public static final ScanResult PASS = new ScanResult( false , null , null );

        public final boolean hit;

        public final SensitiveWord sensitiveWord;

        public final int[] indexes;


        private ScanResult(boolean hit, SensitiveWord sensitiveWord, int[] indexes) {
            this.hit = hit;
            this.sensitiveWord = sensitiveWord;
            this.indexes = indexes;
        }


        public static ScanResult find(SensitiveWord sensitiveWord, int[] indexes ) {
            return new ScanResult( true , sensitiveWord , indexes );
        }



        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ScanResult{");
            sb.append("hit=").append(hit);
            sb.append(", sensitiveWord=").append(sensitiveWord);
            sb.append(", indexes=").append(Arrays.toString(indexes));
            sb.append('}');
            return sb.toString();
        }
    }





    public interface CharMapper {
        char map( char c );
    }


    public interface CharScanner {

        CharScanResult scan( char testChar , char sensitiveChar , String text , Indexes indexes , int ti , SensitiveWord sw , int si );

    }


    public enum CharScanResult {

        SKIP,

        HIT,

        PASS

    }



}
