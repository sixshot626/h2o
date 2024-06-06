#!/bin/sh

path="`pwd`"
repositoryId=h2o-mvn-repo
url="file://${path}/mvn-repo"
ver=6.2.2


mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-common -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-common/target/h2o-common-${ver}.jar
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-dao    -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-dao/target/h2o-dao-${ver}.jar
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-event  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-event/target/h2o-event-${ver}.jar
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-flow   -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-flow/target/h2o-flow-${ver}.jar
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-utils  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-utils/target/h2o-utils-${ver}.jar


mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-common -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-common/target/h2o-common-${ver}.pom
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-dao    -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-dao/target/h2o-dao-${ver}.pom
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-event  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-event/target/h2o-event-${ver}.pom
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-flow   -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-flow/target/h2o-flow-${ver}.pom
mvn deploy:deploy-file  -Durl=$url -DrepositoryId=${repositoryId} -DgroupId=h2o -DartifactId=h2o-utils  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-utils/target/h2o-utils-${ver}.pom
