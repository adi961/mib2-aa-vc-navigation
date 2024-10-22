/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.navsd.functions;

import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.navigation.datatypes.NavigationDistanceToNextManeuver;
import de.vw.mib.bap.mqbab2.common.api.navigation.datatypes.iterator.elements.NavigationManeuverDescriptorElement;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.common.api.system.SystemService;
import de.vw.mib.bap.mqbab2.common.api.system.SystemServiceListener;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.DistanceToNextManeuver_Status;
import de.vw.mib.bap.mqbab2.navsd.api.ASLNavSDConstants;
import java.util.Iterator;

public /* final */ class DistanceToNextManeuver
        extends Function
        implements Property,
        ASLNavSDConstants,
        SystemServiceListener,
        NavigationServiceListener {
    private static /* final */ int NUMBER_OF_VALUES;
    private static /* final */ int WUSCHKE_DISTANCE_KM_CONSTANT;
    private static /* final */ int WUSCHKE_DISTANCE_MILES_CONSTANT;
    private static /* final */ int INVALID_DISTANCE;
    protected static /* final */ int[] SYSTEM_LISTENER_IDS;
    protected static /* final */ int[] NAVIGATION_LISTENER_IDS;
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$DistanceToNextManeuver_Status;

    // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        this.getSystemService().addSystemServiceListener(this, SYSTEM_LISTENER_IDS);
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        return null;
    }

    protected DistanceToNextManeuver_Status dequeueBAPEntity() {
        return (DistanceToNextManeuver_Status) this.context.dequeueBAPEntity(this,
                class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$DistanceToNextManeuver_Status == null
                        ? (class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$DistanceToNextManeuver_Status = DistanceToNextManeuver
                                .class$("de.vw.mib.bap.mqbab2.generated.navsd.serializer.DistanceToNextManeuver_Status"))
                        : class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$DistanceToNextManeuver_Status);
    }

    // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
    }

    // @Override
    public int getFunctionId() {
        return 18;
    }

    private int verifiedBapBargraphSetting() {
        int n;
        NavigationService navigationService = this.getNavigationService();
        NavigationDistanceToNextManeuver navigationDistanceToNextManeuver = navigationService
                .getDistanceToNextManeuver();
        Iterator iterator = navigationService.getManeuverDescriptor();
        if (iterator.hasNext()) {
            NavigationManeuverDescriptorElement navigationManeuverDescriptorElement = (NavigationManeuverDescriptorElement) iterator
                    .next();
            switch (navigationManeuverDescriptorElement.getMainelement()) {
                case 3:
                case 4:
                case 5:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 32:
                case 33:
                case 34:
                case 35: {
                    n = navigationDistanceToNextManeuver.getDistanceToNextManeuverBargraphOnOff();
                    break;
                }
                default: {
                    n = 0;
                    break;
                }
            }
        } else {
            n = navigationDistanceToNextManeuver.getDistanceToNextManeuverBargraphOnOff();
        }
        return n;
    }

    private boolean showDistanceToNextManeuver() {
        boolean bl;
        Iterator iterator = this.getNavigationService().getManeuverDescriptor();
        if (iterator.hasNext()) {
            NavigationManeuverDescriptorElement navigationManeuverDescriptorElement = (NavigationManeuverDescriptorElement) iterator
                    .next();
            switch (navigationManeuverDescriptorElement.getMainelement()) {
                case 8:
                case 9:
                case 10: {
                    bl = false;
                    break;
                }
                default: {
                    bl = true;
                    break;
                }
            }
        } else {
            bl = false;
        }
        return bl;
    }


    private void setDistanceToNextManeuverStatusData(DistanceToNextManeuver_Status distanceToNextManeuver_Status) {
        NavigationService navigationService = this.getNavigationService();
        NavigationDistanceToNextManeuver navigationDistanceToNextManeuver = navigationService.getDistanceToNextManeuver();
        boolean bl = navigationService.getRouteGuidanceState() == 1 || this.getAndroidAutoService().isAndroidAutoRouteGuidanceActive();
        boolean bl2 = navigationService.getDistanceToNextManeuver().getDistanceToNextManeuverDistance() > 0 && bl;
        distanceToNextManeuver_Status.bargraphInfo.bargraphOnOff = this.verifiedBapBargraphSetting();
        this.computeDistanceAndDistanceUnit(distanceToNextManeuver_Status, bl);
        distanceToNextManeuver_Status.bargraphInfo.bargraph = navigationDistanceToNextManeuver.getDistanceToNextManeuverBargraph();
        distanceToNextManeuver_Status.validityInformation.distanceToNextManeuverValid = distanceToNextManeuver_Status.distanceToNextManeuver.unit != 0 && distanceToNextManeuver_Status.distanceToNextManeuver.unit != 3 ? (distanceToNextManeuver_Status.distanceToNextManeuver.unit == 4 ? bl2 && distanceToNextManeuver_Status.distanceToNextManeuver.distance <= 125 : bl2 && distanceToNextManeuver_Status.distanceToNextManeuver.distance <= 200) : bl2;
        if (distanceToNextManeuver_Status.validityInformation.distanceToNextManeuverValid) {
            distanceToNextManeuver_Status.validityInformation.distanceToNextManeuverValid = this.showDistanceToNextManeuver();
        }
    }

    private void sendDistanceToNextManeuverStatus(DistanceToNextManeuver_Status distanceToNextManeuver_Status) {
        this.getDelegate().getPropertyListener(this).statusProperty(distanceToNextManeuver_Status, this);
    }

    // @Override
    public void process(int n) {
        DistanceToNextManeuver_Status distanceToNextManeuver_Status = this.dequeueBAPEntity();
        this.setDistanceToNextManeuverStatusData(distanceToNextManeuver_Status);
        this.sendDistanceToNextManeuverStatus(distanceToNextManeuver_Status);
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
        this.getSystemService().removeSystemServiceListener(this, SYSTEM_LISTENER_IDS);
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

    private void computeDistanceAndDistanceUnit(DistanceToNextManeuver_Status distanceToNextManeuver_Status,
            boolean bl) {
        if (bl) {
            NavigationDistanceToNextManeuver navigationDistanceToNextManeuver = this.getNavigationService()
                    .getDistanceToNextManeuver();
            int[] nArray = new int[2];
            switch (this.getSystemService().getCurrentDistanceUnit()) {
                case 1: {
                    this.getFixFormatter().cnv2Distance2withMinDistanceMiles(
                            navigationDistanceToNextManeuver.getDistanceToNextManeuverDistance(), nArray);
                    break;
                }
                default: {
                    this.getFixFormatter().cnvDistance2KilometersExt(
                            navigationDistanceToNextManeuver.getDistanceToNextManeuverDistance(), nArray);
                }
            }
            distanceToNextManeuver_Status.distanceToNextManeuver.distance = nArray[0];
            distanceToNextManeuver_Status.distanceToNextManeuver.unit = nArray[1];
        } else {
            distanceToNextManeuver_Status.distanceToNextManeuver.distance = -1;
            distanceToNextManeuver_Status.distanceToNextManeuver.unit = 255;
        }
    }

    // @Override
    public void processHMIEvent(int n) {
    }

    // @Override
    public void updateNavigationData(NavigationService navigationService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateSystemData(SystemService systemService, int n) {
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }

    static {
        SYSTEM_LISTENER_IDS = new int[] { 1584 };
        NAVIGATION_LISTENER_IDS = new int[] { 732, 741, 751 };
    }
}
