package peaksoft.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import peaksoft.model.Company;
import peaksoft.repository.CompanyRepository;
import peaksoft.service.CompanyService;

import java.io.IOException;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    @Override
    public void addCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        companyRepository.addCompany(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.getCompanyById(id);
    }

    @Override
    public void updateCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        companyRepository.updateCompany(company);
    }

    @Override
    public void deleteCompany(Company company) {
        companyRepository.deleteCompany(company);
    }

    private void validator(String companyName, String locatedCountry) throws IOException {
        if (companyName.length()>2 && locatedCountry.length()>2) {
            for (Character i : companyName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В названи компании нельзя вставлять цифры");
                }
            }
            for (Character i : locatedCountry.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В названии страны нельзя вставлять цифры");
                }
            }
        }else {
            throw new IOException("В название компании или страны должно быть как минимум 2 буквы");
        }
    }
}
