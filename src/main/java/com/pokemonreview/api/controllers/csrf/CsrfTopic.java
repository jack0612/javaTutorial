package com.pokemonreview.api.controllers.csrf;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class CsrfTopic {
	 @Id
	    @GeneratedValue
	    @Column(name = "TOPIC_ID")
	    Long topicId;

	    @NonNull
	    @Column(name = "TITLE", unique = true, nullable = false)
	    String title;

	    @CreationTimestamp
	    @Column(name = "CREATED", nullable = false)
	    LocalDateTime created;

	    @UpdateTimestamp
	    @Column(name = "UPDATED", nullable = false)
	    LocalDateTime updated;

	    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
	    private List<CsrfPost> posts;
}
