package app.voting.worker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("votes")
public class Vote {

    @Id
    @JsonProperty("voter_id")
    private String id;

    @JsonProperty("vote")
    private String vote;

}
