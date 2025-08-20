# P2P Advertisement Posting Test Cases

## 📋 Overview
Comprehensive test suite for P2P Trading Advertisement Posting functionality. This test suite covers the complete "Post Normal Ad" flow for cryptocurrency trading advertisements on **FCEX Platform**.

## 🌐 Test Environment
- **Base URL**: `https://fcex-fe-718949727112.asia-southeast1.run.app`
- **Test Page**: `/en/p2p/create-ad`
- **Platform**: FCEX Cryptocurrency Exchange

## 🎯 Features Tested

### 📱 Interface Components
- **Navigation**: Back to Ads list, Step indicators (1→2)
- **Tabs**: I want to Buy / I want to Sell toggle
- **Crypto Selection**: Asset (USDT) and Fiat (USD) pairing
- **Pricing**: Fixed price, Market price display, Highest order price
- **Quantity**: Target quantity input with currency display
- **Order Limits**: Min/Max order limits with validation
- **Payment**: Payment method selection (up to 5 methods)
- **Timing**: Payment time limit configuration

## 🧪 Test Cases Details

### 1. **testP2PAdPostPageElementsVisibility**
- **Purpose**: Verify all UI elements are properly displayed on the P2P Ad Post page
- **Key Checks**:
  - ✅ Page title "Post Normal Ad"
  - ✅ Navigation elements (Back button, step indicators)
  - ✅ Buy/Sell tabs visibility
  - ✅ All form labels and input fields
  - ✅ Next Step button presence
- **Expected Result**: All page elements should be visible and accessible

### 2. **testCreateBuyUSDTAdvertisement**
- **Purpose**: Test complete creation of a BUY USDT advertisement
- **Test Data**:
  - Asset: USDT
  - Fiat: USD  
  - Fixed Price: $1.00
  - Target Quantity: 1000 USDT
  - Order Limits: $100 - $500
  - Payment: Bank Transfer
  - Time Limit: 30 minutes
- **Validations**:
  - ✅ Buy tab activation
  - ✅ Market price display ($1)
  - ✅ Currency labels (USDT/USD)
  - ✅ All input values saved correctly
  - ✅ Next Step button enabled
- **Expected Result**: Buy advertisement should be created successfully

### 3. **testCreateSellUSDTAdvertisement**
- **Purpose**: Test complete creation of a SELL USDT advertisement
- **Test Data**:
  - Asset: USDT
  - Fiat: USD
  - Fixed Price: $1.02 (premium for selling)
  - Target Quantity: 2000 USDT
  - Order Limits: $50 - $1000
  - Payment: PayPal
  - Time Limit: 15 minutes
- **Validations**:
  - ✅ Sell tab activation
  - ✅ Higher selling price logic
  - ✅ Different payment method selection
  - ✅ Larger quantity handling
- **Expected Result**: Sell advertisement should be created successfully

### 4. **testFormValidationWithInvalidInputs**
- **Purpose**: Test form validation with various invalid input scenarios
- **Invalid Scenarios**:
  - Empty required fields (Asset/Fiat not selected)
  - Price below minimum (0.01 when range is 0.1-1.1)
  - Negative target quantity (-100)
  - Invalid order limits (min > max: 1000 > 500)
- **Validations**:
  - ✅ Error messages appear for missing fields
  - ✅ Price range validation (0.1 - 1.1)
  - ✅ Quantity validation (no negatives)
  - ✅ Order limit logic validation
- **Expected Result**: Appropriate error messages should prevent invalid submissions

### 5. **testPaymentMethodSelection**
- **Purpose**: Test payment method dropdown functionality and selection limits
- **Payment Methods Tested**:
  - Bank Transfer
  - PayPal
  - Wise / Credit Card
- **Validations**:
  - ✅ Dropdown opens correctly
  - ✅ All payment options visible
  - ✅ Selection limit message ("up to 5 methods")
  - ✅ Method selection works
- **Expected Result**: Payment method selection should work with proper limits

### 6. **testNavigationAndStepProgression**
- **Purpose**: Test navigation flow and step progression through the ad creation process
- **Navigation Flow**:
  1. Start at Step 1 (Set Crypto pair & Payment method)
  2. Fill all required fields
  3. Click "Next Step" → Progress to Step 2
  4. Use "Back to Ads list" → Return to ads management
- **Validations**:
  - ✅ Step 1 active initially
  - ✅ Form completion enables progression
  - ✅ Step 2 activation after Next Step
  - ✅ Back navigation works correctly
- **Expected Result**: Smooth navigation between steps and back to ads list

### 7. **testResponsiveDesignMobileCompatibility**
- **Purpose**: Test responsive design and mobile device compatibility
- **Mobile Testing**:
  - Viewport: 375x667 (iPhone size)
  - Desktop: 1920x1080 (standard desktop)
- **Validations**:
  - ✅ Mobile layout adaptation
  - ✅ Tab switching on mobile
  - ✅ Form inputs accessible on small screens
  - ✅ Button visibility on mobile
  - ✅ Desktop restoration
- **Expected Result**: Interface should work seamlessly across devices

### 8. **testAccessibilityFeatures**
- **Purpose**: Test accessibility compliance and keyboard navigation
- **Accessibility Checks**:
  - Keyboard navigation (Tab key)
  - ARIA attributes (role="tab", aria-label)
  - Form label associations
  - Focus indicators
  - High contrast compatibility
- **Validations**:
  - ✅ Proper ARIA attributes on tabs
  - ✅ Form labels correctly associated
  - ✅ Focus indicators visible
  - ✅ Keyboard navigation works
- **Expected Result**: Interface should meet accessibility standards

## 🚀 Running the Tests

### Run All P2P Ad Post Tests
```bash
mvn test -Dsurefire.suiteXmlFiles=p2p-ad-post-test.xml
```

### Run Specific Test Class
```bash
mvn test -Dtest=P2PAdPostTest
```

### Run Individual Test Method
```bash
mvn test -Dtest=P2PAdPostTest#testCreateBuyUSDTAdvertisement
```

### Run with Browser Configuration
```bash
mvn test -Dsurefire.suiteXmlFiles=p2p-ad-post-test.xml -Dbrowser=chrome
```

## 📊 Test Data Summary

### Buy Advertisement Example:
| Field | Value |
|-------|-------|
| Type | Buy USDT |
| Asset | USDT |
| Fiat | USD |
| Price | $1.00 |
| Quantity | 1000 USDT |
| Min Order | $100 |
| Max Order | $500 |
| Payment | Bank Transfer |
| Time Limit | 30 minutes |

### Sell Advertisement Example:
| Field | Value |
|-------|-------|
| Type | Sell USDT |
| Asset | USDT |
| Fiat | USD |
| Price | $1.02 |
| Quantity | 2000 USDT |
| Min Order | $50 |
| Max Order | $1000 |
| Payment | PayPal |
| Time Limit | 15 minutes |

## ⚠️ Validation Rules Tested

### Price Validation:
- ✅ Range: 0.1 - 1.1 (based on market price)
- ✅ Format: Decimal numbers only
- ✅ Required field validation

### Quantity Validation:
- ✅ Positive numbers only
- ✅ No decimal places for some assets
- ✅ Required field validation

### Order Limit Validation:
- ✅ Min limit ≤ Max limit
- ✅ Both fields required
- ✅ Must be within reasonable range

### Payment Method Validation:
- ✅ At least one method required
- ✅ Maximum 5 methods allowed
- ✅ Valid payment types only

## 🔍 Common Issues to Watch

### UI Issues:
- Dropdown not opening properly
- Tab switching not working
- Mobile layout breaking
- Button states not updating

### Validation Issues:
- Error messages not appearing
- Invalid data being accepted
- Form submission with empty fields
- Price range calculations incorrect

### Navigation Issues:
- Step progression not working
- Back button navigation failing
- Page reload losing form data
- Browser history issues

## 📱 Device Compatibility

### Mobile Devices (375x667):
- ✅ iPhone SE/6/7/8 form factor
- ✅ Touch interface compatibility
- ✅ Responsive layout adaptation
- ✅ Mobile-optimized input fields

### Desktop (1920x1080):
- ✅ Full desktop experience
- ✅ Mouse interaction
- ✅ Keyboard shortcuts
- ✅ Multi-column layouts

## 🔒 Security Considerations

### Data Validation:
- Server-side validation required
- XSS prevention in text inputs
- CSRF protection on form submission
- Rate limiting on ad creation

### Privacy:
- Payment method data protection
- User activity logging
- Personal information handling
- Transaction privacy

## 📈 Performance Metrics

### Page Load Times:
- Target: < 2 seconds initial load
- Form interactions: < 500ms response
- Dropdown population: < 1 second
- Step navigation: < 800ms

### Accessibility Compliance:
- WCAG 2.1 AA compliance
- Keyboard navigation support
- Screen reader compatibility
- Color contrast requirements

## 🔄 Continuous Testing

### Regression Testing:
- Run after UI changes
- Test with new payment methods
- Verify after backend updates
- Check cross-browser compatibility

### Integration Testing:
- Test with actual crypto prices
- Verify payment method integrations
- Check order matching system
- Validate with real user accounts
