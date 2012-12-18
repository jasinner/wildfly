/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2012, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.jboss.as.security;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CODE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_LENGTH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NILLABLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE_TYPE;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.DeprecationData;
import org.jboss.as.controller.ListAttributeDefinition;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParameterValidator;
import org.jboss.as.controller.operations.validation.ParametersOfValidator;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

/**
 * In this class there are all legacy attributes and code to enable backward compatibility with them
 *
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2012 Red Hat Inc.
 */
public class LegacySupport {

    private LegacySupport() {

    }

    /**
     * @author Jason T. Greene
     */
    @Deprecated
    public static class JASPIAuthenticationModulesAttributeDefinition extends ListAttributeDefinition {

        private static final ParameterValidator validator;


        static {
            final ParametersValidator delegate = new ParametersValidator();
            delegate.registerValidator(CODE, new StringLengthValidator(1));
            delegate.registerValidator(Constants.FLAG, new EnumValidator<ModuleFlag>(ModuleFlag.class, true, false));
            delegate.registerValidator(Constants.MODULE, new StringLengthValidator(1, true));
            delegate.registerValidator(Constants.MODULE_OPTIONS, new ModelTypeValidator(ModelType.OBJECT, true));
            delegate.registerValidator(Constants.LOGIN_MODULE_STACK_REF, new StringLengthValidator(1, true));

            validator = new ParametersOfValidator(delegate);
        }


        public JASPIAuthenticationModulesAttributeDefinition() {
            super(Constants.AUTH_MODULES, Constants.AUTH_MODULE, true, false, 1, Integer.MAX_VALUE, validator, null, null, null, true, new DeprecationData(ModelVersion.create(1, 2)), AttributeAccess.Flag.RESTART_ALL_SERVICES);
        }

        @Override
        protected void addValueTypeDescription(ModelNode node, ResourceBundle bundle) {
            // This method being used indicates a misuse of this class
            throw SecurityMessages.MESSAGES.unsupportedOperationExceptionUseResourceDesc();
        }

        @Override
        protected void addAttributeValueTypeDescription(ModelNode node, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, CODE));
            valueType.get(Constants.FLAG, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.FLAG));
            valueType.get(Constants.MODULE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE_OPTIONS));
            valueType.get(Constants.LOGIN_MODULE_STACK_REF, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.LOGIN_MODULE_STACK_REF));
        }

        @Override
        protected void addOperationParameterValueTypeDescription(ModelNode node, String operationName, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, CODE));
            valueType.get(Constants.FLAG, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.FLAG));
            valueType.get(Constants.MODULE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE_OPTIONS));
            valueType.get(Constants.LOGIN_MODULE_STACK_REF, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.LOGIN_MODULE_STACK_REF));
        }

        @Override
        public void marshallAsElement(ModelNode resourceModel, final boolean marshalDefault, XMLStreamWriter writer) throws XMLStreamException {
            throw SecurityMessages.MESSAGES.unsupportedOperation();
        }

        private ModelNode getNoTextValueTypeDescription(final ModelNode parent) {
            final ModelNode valueType = parent.get(VALUE_TYPE);
            final ModelNode code = valueType.get(CODE);
            code.get(DESCRIPTION); // placeholder
            code.get(TYPE).set(ModelType.STRING);
            code.get(NILLABLE).set(false);
            code.get(MIN_LENGTH).set(1);

            final ModelNode flag = valueType.get(Constants.FLAG);
            flag.get(DESCRIPTION);  // placeholder
            flag.get(TYPE).set(ModelType.STRING);
            flag.get(NILLABLE).set(true);
            for (ModuleFlag value : ModuleFlag.values()) { flag.get(ALLOWED).add(value.toString()); }

            final ModelNode module = valueType.get(Constants.MODULE);
            module.get(TYPE).set(ModelType.STRING);
            module.get(NILLABLE).set(true);

            final ModelNode moduleOptions = valueType.get(Constants.MODULE_OPTIONS);
            moduleOptions.get(DESCRIPTION);  // placeholder
            moduleOptions.get(TYPE).set(ModelType.OBJECT);
            moduleOptions.get(VALUE_TYPE).set(ModelType.STRING);
            moduleOptions.get(NILLABLE).set(true);

            final ModelNode ref = valueType.get(Constants.LOGIN_MODULE_STACK_REF);
            ref.get(DESCRIPTION); // placeholder
            ref.get(TYPE).set(ModelType.STRING);
            ref.get(NILLABLE).set(true);
            ref.get(MIN_LENGTH).set(1);

            return valueType;
        }
    }

    /**
     * @author Jason T. Greene
     */
    @Deprecated
    public static class LoginModulesAttributeDefinition extends ListAttributeDefinition {


        public static final ParameterValidator validator;
        public static final ParameterValidator fieldValidator;

        static {
            final ParametersValidator delegate = new ParametersValidator();
            delegate.registerValidator(CODE, new StringLengthValidator(1));
            delegate.registerValidator(Constants.FLAG, new EnumValidator<ModuleFlag>(ModuleFlag.class, false, false));
            delegate.registerValidator(Constants.MODULE, new StringLengthValidator(1, true));
            delegate.registerValidator(Constants.MODULE_OPTIONS, new ModelTypeValidator(ModelType.OBJECT, true));

            validator = new ParametersOfValidator(delegate);
            fieldValidator = delegate;
        }


        public LoginModulesAttributeDefinition(String name, String xmlName) {
            super(name, xmlName, true, false, 1, Integer.MAX_VALUE, validator, null, null, null, true, new DeprecationData(ModelVersion.create(1, 2)), AttributeAccess.Flag.RESTART_ALL_SERVICES);
        }

        @Override
        protected void addValueTypeDescription(ModelNode node, ResourceBundle bundle) {
            // This method being used indicates a misuse of this class
            throw SecurityMessages.MESSAGES.unsupportedOperationExceptionUseResourceDesc();
        }

        @Override
        protected void addAttributeValueTypeDescription(ModelNode node, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, CODE));
            valueType.get(Constants.FLAG, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.FLAG));
            valueType.get(Constants.MODULE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }

        @Override
        protected void addOperationParameterValueTypeDescription(ModelNode node, String operationName, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, CODE));
            valueType.get(Constants.FLAG, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.FLAG));
            valueType.get(Constants.MODULE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }


        @Override
        public void marshallAsElement(ModelNode resourceModel, final boolean marshalDefault, XMLStreamWriter writer) throws XMLStreamException {
            throw SecurityMessages.MESSAGES.unsupportedOperation();
        }

        private ModelNode getNoTextValueTypeDescription(final ModelNode parent) {
            final ModelNode valueType = parent.get(VALUE_TYPE);
            final ModelNode code = valueType.get(CODE);
            code.get(DESCRIPTION); // placeholder
            code.get(TYPE).set(ModelType.STRING);
            code.get(NILLABLE).set(false);
            code.get(MIN_LENGTH).set(1);

            final ModelNode flag = valueType.get(Constants.FLAG);
            flag.get(DESCRIPTION);  // placeholder
            flag.get(TYPE).set(ModelType.STRING);
            flag.get(NILLABLE).set(false);

            for (ModuleFlag value : ModuleFlag.values()) { flag.get(ALLOWED).add(value.toString()); }

            final ModelNode module = valueType.get(Constants.MODULE);
            module.get(TYPE).set(ModelType.STRING);
            module.get(NILLABLE).set(true);

            final ModelNode moduleOptions = valueType.get(Constants.MODULE_OPTIONS);
            moduleOptions.get(DESCRIPTION);  // placeholder
            moduleOptions.get(TYPE).set(ModelType.OBJECT);
            moduleOptions.get(VALUE_TYPE).set(ModelType.STRING);
            moduleOptions.get(NILLABLE).set(true);


            return valueType;
        }
    }

    /**
     * @author Jason T. Greene
     */
    @Deprecated
    public static class MappingModulesAttributeDefinition extends ListAttributeDefinition {

        private static final ParameterValidator validator;
        private static final ParameterValidator fieldValidator;


        static {
            final ParametersValidator delegate = new ParametersValidator();
            delegate.registerValidator(CODE, new StringLengthValidator(1));
            delegate.registerValidator(Constants.TYPE, new StringLengthValidator(1));
            delegate.registerValidator(Constants.MODULE, new StringLengthValidator(1, true));
            delegate.registerValidator(Constants.MODULE_OPTIONS, new ModelTypeValidator(ModelType.OBJECT, true));

            validator = new ParametersOfValidator(delegate);
            fieldValidator = delegate;

        }


        public MappingModulesAttributeDefinition() {
            super(Constants.MAPPING_MODULES, Constants.MAPPING_MODULE, true, false, 1, Integer.MAX_VALUE, validator, null, null, null, true, new DeprecationData(ModelVersion.create(1, 2)), AttributeAccess.Flag.RESTART_ALL_SERVICES);
        }

        @Override
        protected void addValueTypeDescription(ModelNode node, ResourceBundle bundle) {
            // This method being used indicates a misuse of this class
            throw SecurityMessages.MESSAGES.unsupportedOperationExceptionUseResourceDesc();
        }

        @Override
        protected void addAttributeValueTypeDescription(ModelNode node, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, CODE));
            valueType.get(Constants.TYPE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.TYPE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }

        @Override
        protected void addOperationParameterValueTypeDescription(ModelNode node, String operationName, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, CODE));
            valueType.get(Constants.TYPE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.TYPE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }

        @Override
        public void marshallAsElement(ModelNode resourceModel, final boolean marshalDefault, XMLStreamWriter writer) throws XMLStreamException {
            throw SecurityMessages.MESSAGES.unsupportedOperation();
        }

        private ModelNode getNoTextValueTypeDescription(final ModelNode parent) {
            final ModelNode valueType = parent.get(VALUE_TYPE);
            final ModelNode code = valueType.get(CODE);
            code.get(DESCRIPTION); // placeholder
            code.get(TYPE).set(ModelType.STRING);
            code.get(NILLABLE).set(false);
            code.get(MIN_LENGTH).set(1);

            final ModelNode flag = valueType.get(Constants.TYPE);
            flag.get(DESCRIPTION);  // placeholder
            flag.get(TYPE).set(ModelType.STRING);
            flag.get(NILLABLE).set(false);

            final ModelNode moduleOptions = valueType.get(Constants.MODULE_OPTIONS);
            moduleOptions.get(DESCRIPTION);  // placeholder
            moduleOptions.get(TYPE).set(ModelType.OBJECT);
            moduleOptions.get(VALUE_TYPE).set(ModelType.STRING);
            moduleOptions.get(NILLABLE).set(true);


            return valueType;
        }
    }

    /**
     * @author Jason T. Greene
     */
    @Deprecated
    public static class ProviderModulesAttributeDefinition extends ListAttributeDefinition {


        public static final ParameterValidator validator;
        public static final ParameterValidator fieldValidator;

        static {
            final ParametersValidator delegate = new ParametersValidator();
            delegate.registerValidator(CODE, new StringLengthValidator(1));
            delegate.registerValidator(Constants.MODULE_OPTIONS, new ModelTypeValidator(ModelType.OBJECT, true));

            validator = new ParametersOfValidator(delegate);
            fieldValidator = delegate;
        }


        public ProviderModulesAttributeDefinition(String name, String xmlName) {
            super(name, xmlName, true, false, 1, Integer.MAX_VALUE, validator, null, null, null, true, new DeprecationData(ModelVersion.create(1, 2)), AttributeAccess.Flag.RESTART_ALL_SERVICES);
        }

        @Override
        protected void addValueTypeDescription(ModelNode node, ResourceBundle bundle) {
            // This method being used indicates a misuse of this class
            throw SecurityMessages.MESSAGES.unsupportedOperationExceptionUseResourceDesc();
        }

        @Override
        protected void addAttributeValueTypeDescription(ModelNode node, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, CODE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getResourceAttributeValueTypeDescription(getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }

        @Override
        protected void addOperationParameterValueTypeDescription(ModelNode node, String operationName, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
            final ModelNode valueType = getNoTextValueTypeDescription(node);
            valueType.get(CODE, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, CODE));
            valueType.get(Constants.MODULE_OPTIONS, DESCRIPTION).set(resolver.getOperationParameterValueTypeDescription(operationName, getName(), locale, bundle, Constants.MODULE_OPTIONS));
        }


        @Override
        public void marshallAsElement(ModelNode resourceModel, final boolean marshalDefault, XMLStreamWriter writer) throws XMLStreamException {
            throw SecurityMessages.MESSAGES.unsupportedOperation();
        }

        private ModelNode getNoTextValueTypeDescription(final ModelNode parent) {
            final ModelNode valueType = parent.get(VALUE_TYPE);
            final ModelNode code = valueType.get(CODE);
            code.get(DESCRIPTION); // placeholder
            code.get(TYPE).set(ModelType.STRING);
            code.get(NILLABLE).set(false);
            code.get(MIN_LENGTH).set(1);

            final ModelNode moduleOptions = valueType.get(Constants.MODULE_OPTIONS);
            moduleOptions.get(DESCRIPTION);  // placeholder
            moduleOptions.get(TYPE).set(ModelType.OBJECT);
            moduleOptions.get(VALUE_TYPE).set(ModelType.STRING);
            moduleOptions.get(NILLABLE).set(true);

            return valueType;
        }
    }
    /*
     * handlers
     */

    static class LegacyModulesAttributeReader implements OperationStepHandler {
        private String newKeyName;

        LegacyModulesAttributeReader(String newKeyName) {
            this.newKeyName = newKeyName;
        }

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS);
            ModelNode authModules = Resource.Tools.readModel(resource).get(newKeyName);
            ModelNode result = new ModelNode();
            if (authModules.isDefined()) {
                List<Property> loginModules = authModules.asPropertyList();
                for (Property p : loginModules) {
                    result.add(p.getValue());
                }
            }
            context.getResult().set(result);
            context.stepCompleted();
        }
    }

    static class LegacyModulesAttributeWriter implements OperationStepHandler {
        private String newKeyName;

        LegacyModulesAttributeWriter(String newKeyName) {
            this.newKeyName = newKeyName;
        }

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            OperationStepHandler addHandler = context.getResourceRegistration().getSubModel(PathAddress.EMPTY_ADDRESS.append(newKeyName)).getOperationHandler(PathAddress.EMPTY_ADDRESS, "add");
            for (ModelNode module : operation.get(VALUE).asList()) {
                ModelNode addModuleOp = module.clone();
                String code = addModuleOp.get(Constants.CODE).asString();
                PathElement relativePath = PathElement.pathElement(newKeyName, code);
                PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR)).append(relativePath);
                boolean exists = context.readResource(PathAddress.EMPTY_ADDRESS).hasChild(relativePath);
                addModuleOp.get(OP_ADDR).set(address.toModelNode());
                addModuleOp.get(OP).set(ADD);
                context.addStep(addModuleOp, addHandler, OperationContext.Stage.IMMEDIATE);
                if (exists) {
                    ModelNode removeModuleOp = Util.createRemoveOperation(address);
                    context.addStep(removeModuleOp, new SecurityDomainReloadRemoveHandler(), OperationContext.Stage.IMMEDIATE);
                }
                context.stepCompleted();
            }
        }
    }

   /* static class JASPIAuthenticationModulesAttributeWriter implements OperationStepHandler {
        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            OperationStepHandler addHandler = context.getResourceRegistration().getSubModel(PathAddress.EMPTY_ADDRESS.append(AUTH_MODULE)).getOperationHandler(PathAddress.EMPTY_ADDRESS, "add");
            for (ModelNode module : operation.get(VALUE).asList()) {
                ModelNode addModuleOp = module.clone();
                String code = addModuleOp.get(Constants.CODE).asString();
                PathElement relativePath = PathElement.pathElement(AUTH_MODULE, code);
                PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR)).append(relativePath);
                boolean exists = context.readResource(PathAddress.EMPTY_ADDRESS).hasChild(relativePath);
                addModuleOp.get(OP_ADDR).set(address.toModelNode());
                addModuleOp.get(OP).set(ADD);
                context.addStep(addModuleOp, addHandler, OperationContext.Stage.IMMEDIATE);
                if (exists) {
                    ModelNode removeModuleOp = Util.createRemoveOperation(address);
                    context.addStep(removeModuleOp, new SecurityDomainReloadRemoveHandler(), OperationContext.Stage.IMMEDIATE);
                }
                context.stepCompleted();
            }
        }
    }*/
}
