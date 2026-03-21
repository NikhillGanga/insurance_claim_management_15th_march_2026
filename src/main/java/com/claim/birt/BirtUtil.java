package com.claim.birt;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

public class BirtUtil {
    private static IReportEngine engine;
    public static IReportEngine getEngine() throws Exception {
        if (engine == null) {
            EngineConfig config = new EngineConfig();
            config.setEngineHome("C:\\Users\\CEPL\\Downloads\\birt-runtime-4.13.0-20230302\\ReportEngine");
            Platform.startup(config);
            IReportEngineFactory factory =
                (IReportEngineFactory) Platform.createFactoryObject(
                    IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);
        }
        return engine;
    }
}