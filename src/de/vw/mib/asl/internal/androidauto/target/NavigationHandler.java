package de.vw.mib.asl.internal.androidauto.target;

import de.adi961.miblogger.MIBLogger;
import de.vw.mib.asl.api.navbap.ASLNavBAPFactory;
import de.vw.mib.bap.mqbab2.common.api.APIFactory;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationASLDataAdapter;
import org.dsi.ifc.androidauto2.Constants;
import org.dsi.ifc.navigation.BapManeuverDescriptor;



public class NavigationHandler {
    private final ManeuverDescriptorSender maneuverDescriptorSender;

    private NavTurn nextTurn;


    public NavigationHandler() {
        this.maneuverDescriptorSender = new ManeuverDescriptorSender();
    }

    public void navigationFocus(int focus) {
        NavigationASLDataAdapter adapter = (NavigationASLDataAdapter) APIFactory.getAPIFactory().getNavigationService();
        adapter.datapoolValueChanged(733); // Navigation status id

        switch (focus) {
            case Constants.NAVFOCUS_NATIVE: {
                navStopped();
            }
            case Constants.NAVFOCUS_PROJECTED: {
                navStarted();
            }
        }
    }

    private void navStopped() {
        nextTurn = null;
        ASLNavBAPFactory.getNavBAPApi().updateBapTurnToInfo("", "");
        maneuverDescriptorSender.reset();
    }

    private void navStarted() {
        nextTurn = null;
        maneuverDescriptorSender.reset();
    }

    public void handleNextTurnEvent(String road, int turnSide, int event, int turnAngle, int turnNumber) {
        ASLNavBAPFactory.getNavBAPApi().updateBapTurnToInfo(road, "");

        nextTurn = new NavTurn(turnSide, event, turnAngle, turnNumber);
    }

    public void handleUpdateNextTurnDistanceEvent(int distanceMeters, int timeSeconds) {
        MIBLogger.getInstance().trace("distanceMeters: " + distanceMeters + " timeSeconds: " + timeSeconds);

        int unit = distanceMeters < 1000 ? 0 : 1;
        int bargraph = 0;
        boolean bargraphOn = false;

        int bargraphThresholdMeters = getBaragraphThresholdMeters();
        if (distanceMeters <= bargraphThresholdMeters) {
            bargraphOn = true;
            // set bargraph between 0 and 100
            bargraph = (distanceMeters * 100) / bargraphThresholdMeters;
        }

        sendManeuver(distanceMeters, timeSeconds);
        ASLNavBAPFactory.getNavBAPApi().updateBapDistanceToNextManeuver(distanceMeters, unit, bargraphOn, bargraph);
    }


    private int getBaragraphThresholdMeters() {
        // When NAVIGATIONTURNEVENT_OFF_RAMP we assume a highway. Start showing the Bargraph from 2km onwards
        if (nextTurn != null && nextTurn.getEvent() == Constants.NAVIGATIONTURNEVENT_OFF_RAMP) {
            return 2000;
        }

        return 200;
    }

    private void sendManeuver(int distanceMeters, int timeSeconds) {
        if (nextTurn != null && nextTurn.getEvent() == Constants.NAVIGATIONTURNEVENT_OFF_RAMP && distanceMeters <= 2000) {
            sendNextManeuverDescriptor();
            return;
        }

        if (nextTurn != null && distanceMeters <= 200) {
            sendNextManeuverDescriptor();
            return;
        }

        sendFollowRoadManeuverDescriptor();
    }

    private void sendNextManeuverDescriptor() {
        if (nextTurn == null) {
            MIBLogger.getInstance().error("no nextManeuver found");
            return;
        }

        maneuverDescriptorSender.send(nextTurn.toManeuverDescriptor());
    }

    private void sendFollowRoadManeuverDescriptor() {
        BapManeuverDescriptor maneuverDescriptor = new BapManeuverDescriptor();
        maneuverDescriptor.mainElement = ManeuverDescriptorMainElement.FOLLOW_STREET;
        maneuverDescriptor.direction = ManeuverDescriptorDirection.STRAIGHT;

        maneuverDescriptorSender.send(maneuverDescriptor);
    }
}
