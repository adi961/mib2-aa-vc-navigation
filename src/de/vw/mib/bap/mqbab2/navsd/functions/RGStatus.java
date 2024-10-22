/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.navsd.functions;

import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.common.api.androidauto.AndroidAutoService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.RG_Status_Status;
import de.vw.mib.bap.mqbab2.navsd.api.ASLNavSDConstants;

public /* final */ class RGStatus
        extends Function
        implements Property,
        ASLNavSDConstants,
        NavigationServiceListener {
    private Boolean instrumentClusterActionStatus = null;
    protected static /* final */ int[] NAVIGATION_LISTENER_IDS = new int[] { 732, 733 };
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$RG_Status_Status;

    public RGStatus() {
        super();
    }

    // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        return this.computeRGStatusStatus();
    }

    protected RG_Status_Status dequeueBAPEntity() {
        return (RG_Status_Status) this.context.dequeueBAPEntity(this,
                class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$RG_Status_Status == null
                        ? (class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$RG_Status_Status = RGStatus
                                .class$("de.vw.mib.bap.mqbab2.generated.navsd.serializer.RG_Status_Status"))
                        : class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$RG_Status_Status);
    }

    // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
        switch (bAPStage.getFunctionId()) {
            case 34: {
                this.setActionStateRunning((Boolean) object);
                break;
            }
        }
    }

    // @Override
    public int getFunctionId() {
        return 17;
    }

    private void setRouteGuidanceStatus(RG_Status_Status rG_Status_Status) {
        AndroidAutoService androidAutoService = this.getAndroidAutoService();
        System.out.println("AADEBUG: isAndroidAutoRouteGuidanceActive: " + androidAutoService.isAndroidAutoRouteGuidanceActive());
        if (androidAutoService.isAndroidAutoRouteGuidanceActive()) {
            rG_Status_Status.rg_Status = 1;
            return;
        }

        NavigationService navigationService = this.getNavigationService();
        rG_Status_Status.rg_Status = navigationService.getNavigationStatus() != 0 ? 0
                : (this.instrumentClusterActionStatus != null ? (this.instrumentClusterActionStatus
                .booleanValue() != false ? 1 : 0)
                : (navigationService.getRouteGuidanceState() == 0 ? 0 : 1));
    }

    private void sendRouteGuidanceStatus(RG_Status_Status rG_Status_Status) {
        this.getDelegate().getPropertyListener(this).statusProperty(rG_Status_Status, this);
    }

    // @Override
    public void getProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    // @Override
    public void requestAcknowledge() {
    }

    // @Override
    public void errorAcknowledge() {
    }

    // @Override
    public void initialize(boolean bl) {
        this.process(-1);
    }

    // @Override
    public void uninitialize() {
        this.getNavigationService().removeNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
    }

    // @Override
    public void indicationError(int n, BAPFunctionListener bAPFunctionListener) {
    }

    // @Override
    public void setGetProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    // @Override
    public void ackProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    // @Override
    public void process(int n) {
        this.sendRouteGuidanceStatus(this.computeRGStatusStatus());
    }

    private RG_Status_Status computeRGStatusStatus() {
        RG_Status_Status rG_Status_Status = this.dequeueBAPEntity();
        this.setRouteGuidanceStatus(rG_Status_Status);
        return rG_Status_Status;
    }

    protected void setActionStateRunning(Boolean bl) {
        this.instrumentClusterActionStatus = bl;
        this.process(-1);
    }

    // @Override
    public void processHMIEvent(int n) {
    }

    // @Override
    public void updateNavigationData(NavigationService navigationService, int n) {
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }
}
