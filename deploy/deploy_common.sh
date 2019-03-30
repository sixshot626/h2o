#!/bin/sh

path="`pwd`"
repositoryId=h2o-mvn-repo
url="file://${path}/mvn-repo"
ver=5.0.0

mvn deploy:deploy-file -DgroupId=h2o -DartifactId=h2o-common -Dversion=${ver} -Dpackaging=jar -Dfile=../h2o-common/target/h2o-common-${ver}.jar -Durl=$url -DrepositoryId=${repositoryId}

