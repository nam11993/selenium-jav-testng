# Crypto Deposit Test Cases

## Mô tả
Test suite này kiểm tra chức năng tạo địa chỉ ví để deposit cryptocurrency trên platform CEX. Bao gồm các test case sau:

### Functional Tests (`CryptoDepositTest.java`)

1. **testAccessDepositCryptoPage** - Kiểm tra việc truy cập trang Deposit Crypto
2. **testSelectCryptocurrency** - Kiểm tra việc chọn loại cryptocurrency
3. **testSelectNetwork** - Kiểm tra việc chọn network
4. **testGenerateDepositAddress** - Kiểm tra việc tạo địa chỉ deposit
5. **testCopyDepositAddress** - Kiểm tra chức năng copy địa chỉ
6. **testDepositAddressValidation** - Kiểm tra validation với nhiều loại crypto khác nhau
7. **testDepositAddressErrorHandling** - Kiểm tra xử lý lỗi khi thiếu thông tin

### Performance Tests (`CryptoDepositPerformanceTest.java`)

1. **testDepositAddressGenerationPerformance** - Kiểm tra thời gian tạo địa chỉ
2. **testMultipleCryptoAddressGeneration** - Kiểm tra tạo địa chỉ cho nhiều crypto
3. **testConcurrentAddressGeneration** - Kiểm tra tạo địa chỉ liên tiếp

### Page Object Model (`CryptoDepositPage.java`)

Sử dụng Page Object Pattern để:
- Tách biệt logic test và UI elements
- Dễ bảo trì khi UI thay đổi
- Tái sử dụng code

## Cách chạy test

### 1. Chạy tất cả test cases
```bash
mvn test -DsuiteXmlFile=complete-crypto-deposit-test.xml
```

### 2. Chỉ chạy functional tests
```bash
mvn test -DsuiteXmlFile=crypto-deposit-test.xml
```

### 3. Chạy test với browser cụ thể
```bash
mvn test -DsuiteXmlFile=crypto-deposit-test.xml -Dbrowser=chrome
```

### 4. Chạy test class riêng lẻ
```bash
mvn test -Dtest=CryptoDepositTest
mvn test -Dtest=CryptoDepositPerformanceTest
```

### 5. Chạy test method cụ thể
```bash
mvn test -Dtest=CryptoDepositTest#testGenerateDepositAddress
```

## Cấu hình test

### Test Data
- Update thông tin login trong method `loginUser()`:
  - Email: `test@example.com`
  - Password: `testpassword123`

### Supported Browsers
- Chrome (default)
- Firefox

### Timeout Settings
- Implicit wait: 10 giây
- Explicit wait: 15 giây
- Performance test threshold: 30 giây

## Kết quả test

Test sẽ tạo ra:
- Console output với thông tin chi tiết
- TestNG HTML report trong `target/surefire-reports/`
- Screenshots (nếu test fail)

## Lưu ý quan trọng

1. **Credentials**: Cần update email/password test hợp lệ
2. **Environment**: Test được thiết kế cho staging environment
3. **Network**: Cần kết nối internet ổn định
4. **Browser**: Đảm bảo Chrome/Firefox được cài đặt

## Cấu trúc file

```
src/test/java/com/browserstack/
├── CryptoDepositTest.java              # Main functional tests
├── CryptoDepositPerformanceTest.java   # Performance tests
├── LocalBrowserTest.java               # Base test class
└── pages/
    └── CryptoDepositPage.java          # Page Object Model

Test XML files:
├── crypto-deposit-test.xml             # Functional tests only
└── complete-crypto-deposit-test.xml    # All tests
```

## Test Scenarios Covered

### Happy Path
✅ Chọn crypto → Chọn network → Tạo địa chỉ → Copy địa chỉ

### Edge Cases
✅ Nhiều loại cryptocurrency khác nhau
✅ Validation lỗi khi thiếu thông tin
✅ Performance với multiple requests

### Assertions
- Trang load thành công
- Dropdown có options
- Địa chỉ được tạo ra hợp lệ
- Copy functionality hoạt động
- Thời gian response < 30s
- Error handling đúng

## Maintenance

Khi UI thay đổi, chỉ cần update `CryptoDepositPage.java` mà không cần sửa test logic.
