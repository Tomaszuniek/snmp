
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class KolejnyTrapSender {


    public static final String  community  = "public";

    public static final String  trapOid          = ".1.3.6.1.4.1.2021.13.990";

    public static final String  ipAddress      = "172.17.0.4";

    public static final String  agentIPAddress      = "172.17.0.4";

    public static final int     port      = 1161;

    private static final int    specificTrap = 1;


    public static void main(String[] args)
    {
        KolejnyTrapSender snmp4JTrap = new KolejnyTrapSender();

        /* Sending V1 Trap */


        snmp4JTrap.sendSnmpV1Trap();

    }


    public void sendSnmpV1Trap()
    {
        try
        {
            //Create Transport Mapping
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            //Create Target
            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(community));
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            comtarget.setRetries(4);
            comtarget.setTimeout(10000);

            //Create PDU for V1
            PDUv1 pdu = new PDUv1();
            pdu.setType(PDU.V1TRAP);
            pdu.setEnterprise(new OID(trapOid));
            pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC); // 6
            pdu.setSpecificTrap(specificTrap);
            pdu.setAgentAddress(new IpAddress(agentIPAddress));

            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString("Param1"));
            pdu.add(v);

            VariableBinding v2 = new VariableBinding();
            v2.setOid(SnmpConstants.sysName);
            v2.setVariable(new OctetString("Param2"));
            pdu.add(v2);

            VariableBinding v3 = new VariableBinding();
            v3.setOid(SnmpConstants.sysName);
            v3.setVariable(new OctetString("Param3"));
            pdu.add(v3);

            VariableBinding v4 = new VariableBinding();
            v4.setOid(SnmpConstants.sysName);
            v4.setVariable(new OctetString("Param4"));
            pdu.add(v4);

            VariableBinding v5 = new VariableBinding();
            v5.setOid(SnmpConstants.sysName);
            v5.setVariable(new OctetString("Param5"));
            pdu.add(v5);

            VariableBinding v6 = new VariableBinding();
            v6.setOid(SnmpConstants.sysName);
            v6.setVariable(new OctetString("Param6"));
            pdu.add(v6);


            //Send the PDU
            Snmp snmp = new Snmp(transport);
            System.out.println("Sending trap to" + ipAddress + ":" + port);
            snmp.send(pdu, comtarget);
            snmp.close();
            System.out.println("Trap send successfully!!");
        }
        catch (Exception e)
        {
            System.err.println("Error sending Trap to " + ipAddress + ":" + port);
            System.err.println("Exception Message = " + e.getMessage());
        }
    }

}