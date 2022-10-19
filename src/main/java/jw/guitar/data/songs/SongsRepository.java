package jw.guitar.data.songs;

import jw.spigot_fluent_api.data.implementation.RepositoryBase;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;

@Injection(lifeTime = LifeTime.SINGLETON)
public class SongsRepository extends RepositoryBase<Song> {
    public SongsRepository() {
        super(Song.class);
    }
}
