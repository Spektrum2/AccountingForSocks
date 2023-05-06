package sky.pro.accountingforsocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.accountingforsocks.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
