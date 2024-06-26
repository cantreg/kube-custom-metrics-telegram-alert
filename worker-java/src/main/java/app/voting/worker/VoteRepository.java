package app.voting.worker;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface
VoteRepository extends CrudRepository<Vote, String> {
	@Modifying
	@Query("INSERT INTO votes (id, vote) VALUES (:id, :vote) " +
			"ON CONFLICT (id) DO UPDATE SET vote = excluded.vote")
	void insertVote(@Param("id") String voterId, @Param("vote") String voteId);
}
