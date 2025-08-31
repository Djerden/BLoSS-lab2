package com.djeno.lab1.persistence.repositories;

import com.djeno.lab1.persistence.models.TestVirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestVirtualAccountRepository extends JpaRepository<TestVirtualAccount, String> {

}
