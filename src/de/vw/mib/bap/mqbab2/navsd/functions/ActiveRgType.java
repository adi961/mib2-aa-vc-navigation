/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.navsd.functions;

import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.common.api.configuration.ConfigurationService;
import de.vw.mib.bap.mqbab2.common.api.configuration.ConfigurationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.ActiveRgType_SetGet;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.ActiveRgType_Status;
import de.vw.mib.bap.mqbab2.navsd.api.ASLNavSDConstants;

// Decides weather to show map or Bap navigation
public /*final*/ class ActiveRgType
extends Function
implements Property,
ASLNavSDConstants,
NavigationServiceListener,
ConfigurationServiceListener {
    protected static /*final*/ int[] NAVIGATION_LISTENER_IDS = new int[]{733, 1110044, -2147483643, -2147483648};
    private int _currentBapRgType = 3;
    private boolean isWaitingForDsiAck = false;
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ActiveRgType_Status;

   // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        this.getConfigurationService().addConfigurationListener(this, new int[]{-2147459647});
        if (this.getConfigurationService().isMapSwitchingFeatureSelected()) {
            this.setCurrentRgType(0);
        }
        this.determineInternalSwitchState();
        return this.computeActiveRgType();
    }

    protected ActiveRgType_Status dequeueBAPEntity() {
        return (ActiveRgType_Status)this.context.dequeueBAPEntity(this, class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ActiveRgType_Status == null ? (class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ActiveRgType_Status = ActiveRgType.class$("de.vw.mib.bap.mqbab2.generated.navsd.serializer.ActiveRgType_Status")) : class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ActiveRgType_Status);
    }

   // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
    }

   // @Override
    public int getFunctionId() {
        return 39;
    }

   // @Override
    public void process(int n) {
        ActiveRgType_Status activeRgType_Status = this.dequeueBAPEntity();
        this.setCurrentRGType(activeRgType_Status);
        this.sendRgType(activeRgType_Status);
    }

    private ActiveRgType_Status computeActiveRgType() {
        ActiveRgType_Status activeRgType_Status = this.dequeueBAPEntity();
        this.setCurrentRGType(activeRgType_Status);
        return activeRgType_Status;
    }

   // @Override
    public void getProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

   // @Override
    public void requestAcknowledge() {
        this.notifyFsgSetupIfWaitingForAck(new Boolean(false));
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
    }

   // @Override
    public void indicationError(int n, BAPFunctionListener bAPFunctionListener) {
    }

   // @Override
    public void setGetProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        ActiveRgType_SetGet activeRgType_SetGet = (ActiveRgType_SetGet)bAPEntity;
        if (ActiveRgType.inputParametersValid(activeRgType_SetGet)) {
            if (this.getNavigationService().getMapSwitchState() == 0 || this.getNavigationService().getMapSwitchState() == 1 || this.getNavigationService().getMapSwitchState() == 2) {
                this.setCurrentRgType(activeRgType_SetGet.rgtype);
            }
            ActiveRgType_Status activeRgType_Status = this.computeActiveRgType();
            propertyListener.statusProperty(activeRgType_Status, this);
        } else {
            propertyListener.requestError(65, this);
        }
    }

   // @Override
    public void ackProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    private void determineInternalSwitchState() {
        if (this.getLogger().isTraceEnabled(16)) {
            this.getLogger().trace(16).append("getNavigationService().getMapSwitchState() = ").append(this.getNavigationService().getMapSwitchState()).log();
        }
        if (this.getNavigationService().getMapSwitchState() == 1) {
            this.setCurrentRgType(0);
        } else if (this.getNavigationService().getMapSwitchState() == 2) {
            this.setCurrentRgType(3);
        }
    }

    private static boolean inputParametersValid(ActiveRgType_SetGet activeRgType_SetGet) {
        boolean bl;
        switch (activeRgType_SetGet.rgtype) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 255: {
                bl = true;
                break;
            }
            default: {
                bl = false;
            }
        }
        return bl;
    }

    private void setCurrentRGType(ActiveRgType_Status activeRgType_Status) {
        if (this.getAndroidAutoService().isAndroidAutoRouteGuidanceActive()) {
            activeRgType_Status.rgtype = 0;
            return;
        }
        activeRgType_Status.rgtype = !this.getConfigurationService().isMapSwitchingFeatureSelected() ? (this.getNavigationService().getKombiMapStatus() != 0 ? 3 : this.getCurrentRgType()) : this.getCurrentRgType();
        //activeRgType_Status.rgtype = 0;
    }

    private void sendRgType(ActiveRgType_Status activeRgType_Status) {
        this.getDelegate().getPropertyListener(this).statusProperty(activeRgType_Status, this);
    }

    private void notifyFsgSetupIfWaitingForAck(Boolean bl) {
        int[] nArray = new int[]{53};
        this.context.updateStages(this, nArray, bl);
    }

    private void setCurrentRgType(int n) {
        this._currentBapRgType = n;
    }

    private int getCurrentRgType() {
        return this._currentBapRgType;
    }

   // @Override
    public void processHMIEvent(int n) {
    }

   // @Override
    public void updateNavigationData(NavigationService navigationService, int n) {
        if (this.getLogger().isTraceEnabled(16)) {
            this.getLogger().trace(16).append("updateNavigationData(NavigationService, int).getNavigationService().getMapSwitchState() = ").append(this.getNavigationService().getMapSwitchState()).log();
        }
            System.out.println("AADEBUG: n: "+ n +" getMapSwitchState: " + this.getNavigationService().getMapSwitchState());
        if (n == -2147483643) {

            switch (this.getNavigationService().getMapSwitchState()) {
                case 3: {
                    this.setCurrentRgType(0);
                    this.notifyFsgSetupIfWaitingForAck(new Boolean(true));
                    break;
                }
                case 1: {
                    this.setCurrentRgType(0);
                    this.notifyFsgSetupIfWaitingForAck(new Boolean(true));
                    break;
                }
                case 5: {
                    this.setCurrentRgType(3);
                    this.notifyFsgSetupIfWaitingForAck(new Boolean(true));
                    break;
                }
                case 2: {
                    this.setCurrentRgType(3);
                    this.notifyFsgSetupIfWaitingForAck(new Boolean(true));
                    break;
                }
                default: {
                    this.notifyFsgSetupIfWaitingForAck(new Boolean(true));
                }
            }
        }
        this.process(-1);
    }

   // @Override
    public void updateConfigurationData(ConfigurationService configurationService, int n) {
        this.determineInternalSwitchState();
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string)  {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }
}

