package com.networkedassets.condoc.transformer.schedule;

import com.networkedassets.condoc.transformer.manageBundles.core.boundary.BundleManager;
import com.networkedassets.condoc.transformer.produceDocumentation.core.boundary.DocumentationProducer;
import org.quartz.*;

import javax.inject.Inject;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ScheduledJob implements Job {

    @Inject
    DocumentationProducer documentationProducer;

    @Inject
    BundleManager bundleManager;

    private int bundleId;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Starting to process " + this);

        bundleManager.getById(bundleId).ifPresent(
                bundle -> bundle.getSourceUnits().forEach(
                        sourceUnit -> documentationProducer.produceDocumentationForSourceUnit(
                                sourceUnit.getSourceNodeIdentifier()
                        )
                )
        );
    }

    @Override
    public String toString() {
        return "Scheduled event job for bundle: " + bundleId;
    }

    public int getBundleId() {
        return bundleId;
    }

    public void setBundleId(int bundleId) {
        this.bundleId = bundleId;
    }
}
