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
import de.vw.mib.bap.mqbab2.common.api.navigation.datatypes.iterator.elements.NavigationManeuverDescriptorElement;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.ManeuverDescriptor_Status;
import de.vw.mib.bap.mqbab2.generated.navsd.serializer.ManeuverDescriptor_Status$Maneuver_1;
import de.vw.mib.bap.mqbab2.navsd.api.ASLNavSDConstants;
import java.util.Iterator;

public /*final*/ class ManeuverDescriptor
extends Function
implements Property,
ASLNavSDConstants,
NavigationServiceListener {
    private static /*final*/ int MANEUVER_ZERO;
    private static /*final*/ int MANEUVER_ONE;
    private static /*final*/ int MANEUVER_TWO;
    protected static /*final*/ int[] NAVIGATION_LISTENER_IDS;
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ManeuverDescriptor_Status;

   // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        return this.computeManeuverDescriptorStatus();
    }

    protected ManeuverDescriptor_Status dequeueBAPEntity() {
        return (ManeuverDescriptor_Status)this.context.dequeueBAPEntity(this, class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ManeuverDescriptor_Status == null ? (class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ManeuverDescriptor_Status = ManeuverDescriptor.class$("de.vw.mib.bap.mqbab2.generated.navsd.serializer.ManeuverDescriptor_Status")) : class$de$vw$mib$bap$mqbab2$generated$navsd$serializer$ManeuverDescriptor_Status);
    }

   // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
    }

   // @Override
    public int getFunctionId() {
        return 23;
    }

    private int mapToBAPMainElement(int n) {
        int n2;
        switch (n) {
            case 33: {
                n2 = 16;
                break;
            }
            case 32: {
                n2 = 15;
                break;
            }
            default: {
                n2 = n;
            }
        }
        return n2;
    }

    private ManeuverDescriptor_Status$Maneuver_1 validateManeuverData(int n, int n2, String string, int n3) {
        ManeuverDescriptor_Status$Maneuver_1 maneuverDescriptor_Status$Maneuver_1 = new ManeuverDescriptor_Status$Maneuver_1();
        maneuverDescriptor_Status$Maneuver_1.direction = n;
        maneuverDescriptor_Status$Maneuver_1.mainElement = this.mapToBAPMainElement(n2);
        maneuverDescriptor_Status$Maneuver_1.sidestreets.setContent(string);
        maneuverDescriptor_Status$Maneuver_1.zLevelGuidance = n3;
        switch (maneuverDescriptor_Status$Maneuver_1.mainElement) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 8: 
            case 9: 
            case 10: 
            case 34: 
            case 35: 
            case 38: {
                maneuverDescriptor_Status$Maneuver_1.zLevelGuidance = 0;
                maneuverDescriptor_Status$Maneuver_1.sidestreets.setEmptyString();
                break;
            }
            case 11: {
                maneuverDescriptor_Status$Maneuver_1.zLevelGuidance = 0;
                break;
            }
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 25: 
            case 26: 
            case 27: 
            case 28: 
            case 29: 
            case 30: 
            case 31: {
                maneuverDescriptor_Status$Maneuver_1.sidestreets.setEmptyString();
                break;
            }
        }
        return maneuverDescriptor_Status$Maneuver_1;
    }

    private void fillManeuverDescriptor(ManeuverDescriptor_Status maneuverDescriptor_Status) {
        ManeuverDescriptor_Status$Maneuver_1 maneuverDescriptor_Status$Maneuver_1;
        NavigationManeuverDescriptorElement navigationManeuverDescriptorElement;
        NavigationService navigationService = this.getNavigationService();
        Iterator iterator = navigationService.getManeuverDescriptor();
        int n = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++n;
        }
        iterator = navigationService.getManeuverDescriptor();
        if (n > 0) {
            navigationManeuverDescriptorElement = (NavigationManeuverDescriptorElement)iterator.next();
            maneuverDescriptor_Status$Maneuver_1 = this.validateManeuverData(navigationManeuverDescriptorElement.getDirection(), navigationManeuverDescriptorElement.getMainelement(), navigationManeuverDescriptorElement.getSideStreets(), navigationManeuverDescriptorElement.getZLevelGuidance());
            maneuverDescriptor_Status.maneuver_1.direction = maneuverDescriptor_Status$Maneuver_1.direction;
            maneuverDescriptor_Status.maneuver_1.mainElement = maneuverDescriptor_Status$Maneuver_1.mainElement;
            maneuverDescriptor_Status.maneuver_1.sidestreets.setContent(maneuverDescriptor_Status$Maneuver_1.sidestreets);
            maneuverDescriptor_Status.maneuver_1.zLevelGuidance = maneuverDescriptor_Status$Maneuver_1.zLevelGuidance;
        }
        if (n > 1) {
            navigationManeuverDescriptorElement = (NavigationManeuverDescriptorElement)iterator.next();
            maneuverDescriptor_Status$Maneuver_1 = this.validateManeuverData(navigationManeuverDescriptorElement.getDirection(), navigationManeuverDescriptorElement.getMainelement(), navigationManeuverDescriptorElement.getSideStreets(), navigationManeuverDescriptorElement.getZLevelGuidance());
            maneuverDescriptor_Status.maneuver_2.direction = maneuverDescriptor_Status$Maneuver_1.direction;
            maneuverDescriptor_Status.maneuver_2.mainElement = maneuverDescriptor_Status$Maneuver_1.mainElement;
            maneuverDescriptor_Status.maneuver_2.sidestreets.setContent(maneuverDescriptor_Status$Maneuver_1.sidestreets);
            maneuverDescriptor_Status.maneuver_2.zLevelGuidance = maneuverDescriptor_Status$Maneuver_1.zLevelGuidance;
        }
        if (n > 2) {
            navigationManeuverDescriptorElement = (NavigationManeuverDescriptorElement)iterator.next();
            maneuverDescriptor_Status$Maneuver_1 = this.validateManeuverData(navigationManeuverDescriptorElement.getDirection(), navigationManeuverDescriptorElement.getMainelement(), navigationManeuverDescriptorElement.getSideStreets(), navigationManeuverDescriptorElement.getZLevelGuidance());
            maneuverDescriptor_Status.maneuver_3.direction = maneuverDescriptor_Status$Maneuver_1.direction;
            maneuverDescriptor_Status.maneuver_3.mainElement = maneuverDescriptor_Status$Maneuver_1.mainElement;
            maneuverDescriptor_Status.maneuver_3.sidestreets.setContent(maneuverDescriptor_Status$Maneuver_1.sidestreets);
            maneuverDescriptor_Status.maneuver_3.zLevelGuidance = maneuverDescriptor_Status$Maneuver_1.zLevelGuidance;
        }
    }

    private void sendManeuverDescriptor(ManeuverDescriptor_Status maneuverDescriptor_Status) {
        this.getDelegate().getPropertyListener(this).statusProperty(maneuverDescriptor_Status, this);
    }

   // @Override
    public void process(int n) {
        this.sendManeuverDescriptor(this.computeManeuverDescriptorStatus());
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

    private ManeuverDescriptor_Status computeManeuverDescriptorStatus() {
        ManeuverDescriptor_Status maneuverDescriptor_Status = this.dequeueBAPEntity();
        if (this.getNavigationService().getRouteGuidanceState() != 0 || this.getAndroidAutoService().isAndroidAutoRouteGuidanceActive()) {
            this.fillManeuverDescriptor(maneuverDescriptor_Status);
        }
        return maneuverDescriptor_Status;
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
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }

    static {
        NAVIGATION_LISTENER_IDS = new int[]{732, 751};
    }
}

