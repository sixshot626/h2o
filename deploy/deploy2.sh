#!/bin/sh

path="`pwd`"
repositoryId=smarthome-releases
url="https://packages.aliyun.com/maven/repository/2100913-release-ySXZ77/"
ver=6.1.0


mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=butterfly-container   -Dversion=${ver} -Dpackaging=jar -Dfile=../butterfly-container/target/butterfly-container-${ver}.jar     -Durl=$url -DrepositoryId=${repositoryId}
mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=butterfly-persistence -Dversion=${ver} -Dpackaging=jar -Dfile=../butterfly-persistence/target/butterfly-persistence-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}

mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-common -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-common/target/h2o-common-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}
mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-dao    -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-dao/target/h2o-dao-${ver}.jar       -Durl=$url -DrepositoryId=${repositoryId}
mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-event  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-event/target/h2o-event-${ver}.jar   -Durl=$url -DrepositoryId=${repositoryId}
mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-flow   -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-flow/target/h2o-flow-${ver}.jar     -Durl=$url -DrepositoryId=${repositoryId}
mvn -s settings.xml   deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-utils  -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-utils/target/h2o-utils-${ver}.jar   -Durl=$url -DrepositoryId=${repositoryId}

