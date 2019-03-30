#!/bin/sh

path="`pwd`"
repositoryId=h2o-mvn-repo
url="file://${path}/mvn-repo"
ver=5.0.0


mvn deploy:deploy-file -DgroupId=h2o -DartifactId=butterfly-container -Dversion=${ver} -Dpackaging=jar -Dfile=../butterfly-container/target/butterfly-container-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn deploy:deploy-file -DgroupId=h2o -DartifactId=butterfly-persistence -Dversion=${ver} -Dpackaging=jar -Dfile=../butterfly-persistence/target/butterfly-persistence-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}

mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-common -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-common/target/h2o-common-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-dao -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-dao/target/h2o-dao-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-event -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-event/target/h2o-event-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-flow -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-flow/target/h2o-flow-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-utils -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-utils/target/h2o-utils-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}

