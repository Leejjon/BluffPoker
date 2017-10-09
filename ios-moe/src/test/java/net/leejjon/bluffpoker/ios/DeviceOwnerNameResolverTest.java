package net.leejjon.bluffpoker.ios;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeviceOwnerNameResolverTest {
    @Test
    public void testResolveDeviceOwnerName() {
        assertEquals("Leejjon", DeviceOwnerNameResolver.resolveDeviceOwnerName("Leejjon\u2019s iPhone"));
        assertEquals("Leejjon", DeviceOwnerNameResolver.resolveDeviceOwnerName("Leejjon's iPhone"));
        assertEquals("Leejjon", DeviceOwnerNameResolver.resolveDeviceOwnerName("iPhone van Leejjon"));
    }
}
