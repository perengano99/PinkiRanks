package com.perengano99.PinkiRanks.NametagAPI.packets;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.perengano99.PinkiRanks.PC;

import org.apache.commons.lang.Validate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GameProfileWrapper {

    public static GameProfileWrapper fromHandle(Object object) {
        Validate.isTrue(object instanceof GameProfile, "object is not a GameProfile");
        GameProfile gameProfile = (GameProfile) object;
        GameProfileWrapper wrapper = new GameProfileWrapper(gameProfile.getId(), gameProfile.getName());
        for (Map.Entry<String, Collection<Property>> entry : gameProfile.getProperties().asMap().entrySet()) {
            for (Property property : entry.getValue()) {
                wrapper.getProperties().put(entry.getKey(), PropertyWrapper.fromHandle(property));
            }
        }
        return wrapper;
    }

    private final UUID uuid;
    private final String name;
    private final Multimap<String, PropertyWrapper> properties = LinkedHashMultimap.create();

    public GameProfileWrapper(UUID uuid, String name) {
        Validate.notNull(uuid, "uuid cannot be null");
        Validate.notNull(name, "name cannot be null");
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Multimap<String, PropertyWrapper> getProperties() {
        return properties;
    }

    public Object getHandle() {
        GameProfile gameProfile = new GameProfile(this.uuid, this.name);
        for (Map.Entry<String, Collection<PropertyWrapper>> entry : properties.asMap().entrySet()) {
            for (PropertyWrapper wrapper : entry.getValue()) {
                gameProfile.getProperties().put(entry.getKey(), (Property) wrapper.getHandle());
            }
        }
        return gameProfile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GameProfileWrapper)) {
            return false;
        }
        GameProfileWrapper gameProfile = (GameProfileWrapper) obj;
        return gameProfile.uuid.equals(this.uuid) && gameProfile.name.equals(this.name) && gameProfile.properties.equals(this.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, properties);
    }

    @Override
    public String toString() {
        return "GameProfileWrapper{uuid=" + this.uuid + ",name=" + this.name + ",properties=" + this.properties + "}";
    }

    public static class PropertyWrapper {

        public static PropertyWrapper fromHandle(Object object) {
            Validate.isTrue(object instanceof Property, "object " + object + " is not a Property");
            Property property = (Property) object;
            return new PropertyWrapper(property.getName(), property.getValue(), property.getSignature());
        }

        private final String name;
        private final String value;
        private final String signature;

        public PropertyWrapper(String name, String value, String signature) {
            this.name = PC.p.nocolor(name);
            this.value = value;
            this.signature = signature;
        }

        public String getName() {
            return PC.p.nocolor(name);
        }

        public String getValue() {
            return value;
        }

        public boolean hasSignature() {
            return signature != null;
        }

        public String getSignature() {
            return signature;
        }

        public Object getHandle() {
            return new Property(PC.p.nocolor(this.name), this.value, this.signature);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof PropertyWrapper)) {
                return false;
            }
            PropertyWrapper property = (PropertyWrapper) obj;
            if (property.name.equals(PC.p.nocolor(this.name)) && property.value.equals(this.value)) {
                if (property.hasSignature() && this.hasSignature()) {
                    return property.signature.equals(this.signature);
                } else if (!property.hasSignature() && !this.hasSignature()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(PC.p.nocolor(this.name), value, signature);
        }

        @Override
        public String toString() {
            return "Property{name=" + PC.p.nocolor(this.name) + ",value=" + this.value + ",signature=" + this.signature + "}";
        }
    }
}
