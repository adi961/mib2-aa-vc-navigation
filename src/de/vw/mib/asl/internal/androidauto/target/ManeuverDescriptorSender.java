package de.vw.mib.asl.internal.androidauto.target;

import de.vw.mib.asl.framework.internal.framework.GenericCollector;
import generated.de.vw.mib.asl.internal.ListManager;
import org.dsi.ifc.navigation.BapManeuverDescriptor;

import java.util.Vector;

public class ManeuverDescriptorSender {

    private BapManeuverDescriptor lastManeuverDescriptor;

    public void send(BapManeuverDescriptor bapManeuverDescriptor) {
        if (lastManeuverDescriptor != null && lastManeuverDescriptor.toString().equals(bapManeuverDescriptor.toString())) {
            System.out.println("AADEBUG: same maneuver descriptor. Not sending.");
            return;
        }

        sendBapManeuverDescriptor(new BapManeuverDescriptor[]{bapManeuverDescriptor});
        this.lastManeuverDescriptor = bapManeuverDescriptor;
    }

    private void sendBapManeuverDescriptor(BapManeuverDescriptor[] bapManeuverDescriptorArray) {
        BapManeuverDescriptor[] bapManeuverDescriptorArray2 = bapManeuverDescriptorArray;
        if (bapManeuverDescriptorArray2 == null || bapManeuverDescriptorArray2.length == 0) {
            bapManeuverDescriptorArray2 = new BapManeuverDescriptor[]{new BapManeuverDescriptor(0, 0, 0, new byte[0])};
        }

        Vector object = new Vector();
        GenericCollector[] genericCollectorArray = new GenericCollector[bapManeuverDescriptorArray2.length];
        for (int i3 = 0; i3 < bapManeuverDescriptorArray2.length; ++i3) {
            GenericCollector genericCollector = new GenericCollector();
            genericCollector.setIntItem(0, bapManeuverDescriptorArray2[i3].getMainElement());
            genericCollector.setIntItem(1, bapManeuverDescriptorArray2[i3].getDirection());
            genericCollector.setIntItem(2, bapManeuverDescriptorArray2[i3].getZLevelGuidance());
            String string = "";
            byte[] byArray = bapManeuverDescriptorArray2[i3].getSideStreets();
            if (byArray != null) {
                string = new String(ManeuverDescriptorSender.bytesToCharacters(byArray));
            }
            genericCollector.setStringItem(3, string);
            ((Vector) object).add(genericCollector);
            genericCollectorArray[i3] = genericCollector;
        }

        ListManager.getGenericASLList(751).updateList(genericCollectorArray);
        ListManager.getGenericASLList(838).updateList(genericCollectorArray);
    }

    private static char[] bytesToCharacters(byte[] byArray) {
        int n = byArray.length;
        char[] cArray = new char[n];
        for (int i2 = 0; i2 < n; ++i2) {
            cArray[i2] = (char) (byArray[i2] & 0xFF);
        }
        return cArray;
    }
}
