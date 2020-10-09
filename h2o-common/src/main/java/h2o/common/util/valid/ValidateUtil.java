package h2o.common.util.valid;

import h2o.common.lang.tuple.Tuple2;
import h2o.common.lang.tuple.TupleUtil;
import h2o.common.util.collection.ListBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ValidateUtil {
	
	private ValidateUtil() {}
	
	public static List<Tuple2<Object,String>> validate( Object bean , Validator... validators   ) {
		
		List<Tuple2<Object,String>> rs = ListBuilder.newList();
		Set<Object> ks = new HashSet<Object>();
		for( Validator validator : validators ) {
			
			Object k = validator.getK();
			if( ks.contains(k) ) {
				continue;
			}
			if( !validator.validate(bean) ) {
				rs.add( TupleUtil.t( k , validator.getMessage() ) );
				ks.add(k);
			}
		}
		
		return rs;
	}
	
	public static Tuple2<String,String> validateOne( Object bean , Validator... validators  ) {
		
		for( Validator validator : validators ) {
			if( !validator.validate(bean) ) {
				return TupleUtil.t( validator.getK() ,  validator.getMessage() );
			}
		}
		return null;
	}

}
