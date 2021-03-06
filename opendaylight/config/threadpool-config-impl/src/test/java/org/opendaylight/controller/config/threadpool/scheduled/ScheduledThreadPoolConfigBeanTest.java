package org.opendaylight.controller.config.threadpool.scheduled;

import org.junit.Before;
import org.junit.Test;
import org.opendaylight.controller.config.api.ConflictingVersionException;
import org.opendaylight.controller.config.api.ValidationException;
import org.opendaylight.controller.config.api.jmx.CommitStatus;
import org.opendaylight.controller.config.manager.impl.AbstractConfigTest;
import org.opendaylight.controller.config.manager.impl.factoriesresolver.HardcodedModuleFactoriesResolver;
import org.opendaylight.controller.config.util.ConfigTransactionJMXClient;
import org.opendaylight.controller.config.yang.threadpool.impl.NamingThreadFactoryModuleFactory;
import org.opendaylight.controller.config.yang.threadpool.impl.NamingThreadFactoryModuleMXBean;
import org.opendaylight.controller.config.yang.threadpool.impl.scheduled.ScheduledThreadPoolModuleFactory;
import org.opendaylight.controller.config.yang.threadpool.impl.scheduled.ScheduledThreadPoolModuleMXBean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ScheduledThreadPoolConfigBeanTest extends AbstractConfigTest {

    private ScheduledThreadPoolModuleFactory factory;
    private final String instanceName = "scheduled1";

    @Before
    public void setUp() {

        factory = new ScheduledThreadPoolModuleFactory();
        super.initConfigTransactionManagerImpl(new HardcodedModuleFactoriesResolver(factory,
                new NamingThreadFactoryModuleFactory()));
    }

    @Test
    public void testCreateBean() throws InstanceAlreadyExistsException, ValidationException,
            ConflictingVersionException {
        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();

        createScheduled(transaction, instanceName, 1);
        transaction.validateConfig();
        CommitStatus status = transaction.commit();

        assertBeanCount(1, factory.getImplementationName());
        assertStatus(status, 2, 0, 0);
    }

    @Test
    public void testReusingOldInstance() throws InstanceAlreadyExistsException, ConflictingVersionException,
            ValidationException {

        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();
        createScheduled(transaction, instanceName, 1);

        transaction.commit();

        assertBeanCount(1, factory.getImplementationName());

        transaction = configRegistryClient.createTransaction();
        CommitStatus status = transaction.commit();

        assertBeanCount(1, factory.getImplementationName());
        assertStatus(status, 0, 0, 2);
    }

    @Test
    public void testReconfigurationInstance() throws InstanceAlreadyExistsException, ValidationException,
            ConflictingVersionException, InstanceNotFoundException {
        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();
        createScheduled(transaction, instanceName, 1);

        transaction.commit();

        transaction = configRegistryClient.createTransaction();
        ObjectName databaseNew = transaction.lookupConfigBean(factory.getImplementationName(), instanceName);
        ScheduledThreadPoolModuleMXBean proxy = transaction.newMXBeanProxy(databaseNew,
                ScheduledThreadPoolModuleMXBean.class);
        proxy.setMaxThreadCount(99);

        CommitStatus status = transaction.commit();

        assertBeanCount(1, factory.getImplementationName());
        assertStatus(status, 0, 1, 1);
    }

    @Test
    public void testDestroy() throws InstanceAlreadyExistsException, ValidationException, ConflictingVersionException,
            InstanceNotFoundException {
        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();
        createScheduled(transaction, instanceName, 1);

        transaction.commit();

        transaction = configRegistryClient.createTransaction();
        transaction.destroyConfigBean(factory.getImplementationName(), instanceName);
        CommitStatus status = transaction.commit();

        assertBeanCount(0, factory.getImplementationName());
        assertStatus(status, 0, 0, 1);
    }

    @Test
    public void testInstanceAlreadyExistsException() throws ConflictingVersionException, ValidationException,
            InstanceAlreadyExistsException {
        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();
        createScheduled(transaction, instanceName, 1);
        transaction.commit();
        transaction = configRegistryClient.createTransaction();
        try {
            createScheduled(transaction, instanceName, 2);
            fail();
        } catch (InstanceAlreadyExistsException e) {
            assertThat(
                    e.getMessage(),
                    containsString("There is an instance registered with name ModuleIdentifier{factoryName='threadpool-scheduled', instanceName='scheduled1'}"));
        }
    }

    @Test
    public void testValidationException() throws InstanceAlreadyExistsException {
        ConfigTransactionJMXClient transaction = configRegistryClient.createTransaction();
        createScheduled(transaction, instanceName, 0);

        try {
            transaction.validateConfig();
            fail();
        } catch (ValidationException e) {
            assertTrue(e.getFailedValidations().containsKey(factory.getImplementationName()));
            assertEquals(1, e.getFailedValidations().get(factory.getImplementationName()).keySet().size());
        }
    }

    private ObjectName createScheduled(ConfigTransactionJMXClient transaction, String instanceName, int maxThreadCount)
            throws InstanceAlreadyExistsException {
        ObjectName nameCreated = transaction.createModule(factory.getImplementationName(), instanceName);
        ScheduledThreadPoolModuleMXBean mxBean = transaction.newMBeanProxy(nameCreated,
                ScheduledThreadPoolModuleMXBean.class);
        mxBean.setMaxThreadCount(maxThreadCount);

        ObjectName threadFactoryON = transaction.createModule(NamingThreadFactoryModuleFactory.NAME, "naming");
        NamingThreadFactoryModuleMXBean namingThreadFactoryModuleMXBean = transaction.newMXBeanProxy(threadFactoryON,
                NamingThreadFactoryModuleMXBean.class);
        namingThreadFactoryModuleMXBean.setNamePrefix("prefix");

        mxBean.setThreadFactory(threadFactoryON);

        return nameCreated;
    }

}
