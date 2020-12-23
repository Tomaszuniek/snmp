import java.util.Date;

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

public class TrapSender
{
    public static final String  community  = "public";

    public static final String  trapOid          = ".1.3.6.1.4.1.2021.13.990";

    public static final String  ipAddress      = "127.0.0.1";

    public static final int     port      = 1161;

    public TrapSender()
    {
    }

    public static void main(String[] args)
    {
        TrapSender snmp4JTrap = new TrapSender();

        snmp4JTrap.sendSnmpV1Trap();

        snmp4JTrap.sendSnmpV2Trap();
    }

    public void sendSnmpV1Trap()
    {
        try
        {
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(community));
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDUv1 pdu = new PDUv1();
            pdu.setType(PDU.V1TRAP);
            pdu.setEnterprise(new OID(trapOid));
            pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
            pdu.setSpecificTrap(1);
            pdu.setAgentAddress(new IpAddress(ipAddress));

            Snmp snmp = new Snmp(transport);
            System.out.println("Sending V1 Trap to " + ipAddress + " on Port " + port);
            snmp.send(pdu, comtarget);
            snmp.close();
        }
        catch (Exception e)
        {
            System.err.println("Error in Sending V1 Trap to " + ipAddress + " on Port " + port);
            System.err.println("Exception Message = " + e.getMessage());
        }
    }


    public void sendSnmpV2Trap()
    {
        try
        {
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(community));
            comtarget.setVersion(SnmpConstants.version2c);
            comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDU pdu = new PDU();

            pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));

            pdu.add(new VariableBinding(new OID(trapOid), new OctetString("Major")));
            pdu.setType(PDU.NOTIFICATION);

            Snmp snmp = new Snmp(transport);
            System.out.println("Sending V2 Trap to " + ipAddress + " on Port " + port);
            snmp.send(pdu, comtarget);
            snmp.close();
        }
        catch (Exception e)
        {
            System.err.println("Error in Sending V2 Trap to " + ipAddress + " on Port " + port);
            System.err.println("Exception Message = " + e.getMessage());
        }
    }
}