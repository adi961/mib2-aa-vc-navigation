/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.navsd.functions;

import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.common.api.androidauto.AndroidAutoService;
import de.vw.mib.bap.mqbab2.common.api.androidauto.AndroidAutoServiceListener;
import de.vw.mib.bap.mqbab2.common.api.carlife.CarLifeService;
import de.vw.mib.bap.mqbab2.common.api.carlife.CarLifeServiceListener;
import de.vw.mib.bap.mqbab2.common.api.carplay.CarPlayService;
import de.vw.mib.bap.mqbab2.common.api.carplay.CarPlayServiceListener;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.InfoStates_Status;
import de.vw.mib.bap.mqbab2.navsd.api.ASLNavSDConstants;

// Shows the smartphone navigation active screen
public /*final*/ class InfoStates
extends Function
implements Property,
ASLNavSDConstants,
NavigationServiceListener,
CarPlayServiceListener,
CarLifeServiceListener,
AndroidAutoServiceListener {
    protected static /*final*/ int[] NAVIGATION_LISTENER_IDS = new int[]{734, 733, 3937};
    protected static /*final*/ int[] CAR_PLAY_LISTENER_IDS = new int[]{1109328000};
    protected static /*final*/ int[] CAR_LIFE_LISTENER_IDS = new int[]{1332161536};
    protected static /*final*/ int[] ANDROID_AUTO_LISTENER_IDS = new int[]{895953920};
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$InfoStates_Status;

   // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        this.getCarPlayService().addCarPlayServiceListener(this, CAR_PLAY_LISTENER_IDS);
        this.getCarLifeService().addCarLifeServiceListener(this, CAR_LIFE_LISTENER_IDS);
        this.getAndroidAutoService().addAndroidAutoServiceListener(this, ANDROID_AUTO_LISTENER_IDS);
        return null;
    }

    protected InfoStates_Status dequeueBAPEntity() {
        return (InfoStates_Status)this.context.dequeueBAPEntity(this, class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$InfoStates_Status == null ? (class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$InfoStates_Status = InfoStates.class$("de.vw.mib.bap.mqbab2.generated.navsd.serializer.InfoStates_Status")) : class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$InfoStates_Status);
    }

   // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
    }

   // @Override
    public int getFunctionId() {
        return 38;
    }

   // @Override
    public void process(int n) {
        int n2 = this.getCurrentBAPInfoState();
        InfoStates_Status infoStates_Status = this.dequeueBAPEntity();
        infoStates_Status.states = n2;
        this.getDelegate().getPropertyListener(this).statusProperty(infoStates_Status, this);
    }

    private int getCurrentBAPInfoState() {
        int n;
        if (this.getAndroidAutoService().isAndroidAutoRouteGuidanceActive()) {
            return 0;
        }
        if (this.getCarPlayService().isCarPlayRouteGuidanceActive() || this.getCarLifeService().isCarLifeRouteGuidanceActive()) {
            n = 6;
        } else {
            NavigationService navigationService = this.getNavigationService();
            switch (navigationService.getNavigationStatus()) {
                case 0: {
                    if (!navigationService.isGPSSignalAvailable() && !navigationService.isNavigationDemoModeActive()) {
                        n = 3;
                        break;
                    }
                    n = 0;
                    break;
                }
                case 1: {
                    n = 1;
                    break;
                }
                case 2: {
                    n = 2;
                    break;
                }
                default: {
                    n = 0;
                }
            }
        }
        return n;
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
    }

   // @Override
    public void uninitialize() {
        this.getNavigationService().removeNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        this.getCarPlayService().removeCarPlayServiceListener(this, CAR_PLAY_LISTENER_IDS);
        this.getCarLifeService().removeCarLifeServiceListener(this, CAR_LIFE_LISTENER_IDS);
        this.getAndroidAutoService().removeAndroidAutoServiceListener(this, ANDROID_AUTO_LISTENER_IDS);
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
    public void processHMIEvent(int n) {
    }

   // @Override
    public void updateAndroidAutoData(AndroidAutoService androidAutoService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateCarPlayData(CarPlayService carPlayService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateCarLifeData(CarLifeService carLifeService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateNavigationData(NavigationService navigationService, int n) {
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }
}

