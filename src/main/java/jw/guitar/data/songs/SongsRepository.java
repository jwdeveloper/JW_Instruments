package jw.guitar.data.songs;

import jw.fluent_api.desing_patterns.unit_of_work.implementation.RepositoryBase;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;

@Injection(lifeTime = LifeTime.SINGLETON)
public class SongsRepository extends RepositoryBase<Song> {
    public SongsRepository() {
        super(Song.class);
    }
}
