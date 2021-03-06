package org.opendaylight.controller.sal.binding.test;

import junit.framework.Assert;


import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.Augmentation;

public class AugmentationVerifier<T extends Augmentable<T>> {

    private T object;

    public AugmentationVerifier(T objectToVerify) {
        this.object = objectToVerify;
    }

    public AugmentationVerifier<T> assertHasAugmentation(Class<? extends Augmentation<T>> augmentation) {
        assertHasAugmentation(object, augmentation);
        return (AugmentationVerifier<T>) this;
    }

    public static <T extends Augmentable<T>> void assertHasAugmentation(T object,
            Class<? extends Augmentation<T>> augmentation) {
        Assert.assertNotNull(object);
        Assert.assertNotNull("Augmentation " + augmentation.getSimpleName() + " is not present.", object.getAugmentation(augmentation));
    }

    public static <T extends Augmentable<T>> AugmentationVerifier<T> from(T obj) {
        return new AugmentationVerifier<T>(obj);
    }

}
