package com.mmiholdings.k8.plugin.kubernetes;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;

@Mojo(name = "deploy")
public class DeployK8Mojo extends AbstractKubernetesMojo {

    @Override
    public void execute() throws MojoExecutionException {

        if (kubernetesConfigmapExist()) {
            createConfigs();
        }
        info("Deploying kubernetes components ....");
        runKubeControl(CREATE,persistenceFileName);
        runKubeControl(CREATE,claimFileName);
        runKubeControl(CREATE,deploymentFileName);
        runKubeControl(CREATE,serviceFileName);  
    }

    private void createConfigs() {
        File folder = getKubernetesConfigFolder();
        if (folder.list().length>0) {
            String configMapName = artefactName;
            info("creating configmap for " + configMapName);
            try {
                runKubeConfigMap(configMapName,folder.getAbsolutePath());
            } catch (MojoExecutionException e) {
                error("file error",e);
            }
        }
    }

}