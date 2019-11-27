package io.stacs.nav.dapp.maven.mojo.utils;

import com.alipay.sofa.ark.tools.ArtifactItem;
import org.apache.maven.artifact.Artifact;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author dekuofa <br>
 * @date 2019-11-26 <br>
 */
public class Artifacts {

    private static final Function<List<ArtifactItem>, Predicate<Artifact>> EXCLUDE_FILTER =
        excludes -> artifact -> excludes.stream()
            .noneMatch(exclude -> exclude.isSameIgnoreVersion(ArtifactItem.parseArtifactItem(artifact)));

    private static final Function<Set<String>, Predicate<Artifact>> EXCLUDE_GROUP_ID_FILTER =
        excludes -> artifact -> excludes == null || !excludes.contains(artifact.getGroupId());
    private static final Function<Set<String>, Predicate<Artifact>> EXCLUDE_ARTIFACT_ID_FILTER =
        excludes -> artifact -> excludes == null || !excludes.contains(artifact.getArtifactId());

    public static Set<Artifact> filterExcludeArtifacts(Set<Artifact> artifacts, LinkedHashSet<String> excludes,
        LinkedHashSet<String> excludeGroupIds, LinkedHashSet<String> excludeArtifactIds) {
        List<ArtifactItem> excludeList =
            excludes.stream().map(ArtifactItem::parseArtifactItemIgnoreVersion).collect(Collectors.toList());

        return artifacts.stream().filter(EXCLUDE_FILTER.apply(excludeList))
            .filter(EXCLUDE_GROUP_ID_FILTER.apply(excludeGroupIds)).
                filter(EXCLUDE_ARTIFACT_ID_FILTER.apply(excludeArtifactIds)).collect(Collectors.toSet());
    }

}
