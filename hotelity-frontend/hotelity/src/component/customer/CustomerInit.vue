<script setup>
import {useRouter} from 'vue-router';
import {onMounted, ref, watch, computed} from 'vue';
import * as api from '@/api/apiService.js';

const router = useRouter();

const isLoading = ref(true);
const countries = ref([]);

const form = ref({
  customerName: '',
  customerEmail: '',
  customerPhoneNumber: '',
  customerEnglishName: '',
  customerAddress: '',
  customerInfoAgreement: 0,
  customerStatus: 0,
  customerType: '',
  nationCodeFk: 1,
  customerGender: ''
});

let initialFormState = {
  customerName: '',
  customerEmail: '',
  customerPhoneNumber: '',
  customerEnglishName: '',
  customerAddress: '',
  customerInfoAgreement: 0,
  customerStatus: 0,
  customerType: '',
  nationCodeFk: 1,
  customerGender: ''
};
/*
watch(() => form.customerPhoneNumber, (newVal) => {
  let phoneNumber = newVal.replace(/[^0-9]/g, '');
  let autoHyphenPhone = '';

  if (phoneNumber.length < 4) {
    autoHyphenPhone = phoneNumber;
  } else if (phoneNumber.length < 7) {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3)}`;
  } else if (phoneNumber.length < 11) {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3, 3)}-${phoneNumber.substr(6)}`;
  } else {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3, 4)}-${phoneNumber.substr(7)}`;
  }

  form.customerPhoneNumber = autoHyphenPhone;
}, { deep: true });
*/
async function handleSubmit() {

  // Convert the phone number to the correct format
  // let phoneNumber = form.value.customerPhoneNumber.replace(/[^0-9]/g, '');
  let phoneNumber = form.value.customerPhoneNumber.replace(/-/g, '');
  let autoHyphenPhone = '';

  if (phoneNumber.length < 4) {
    autoHyphenPhone = phoneNumber;
  } else if (phoneNumber.length < 7) {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3)}`;
  } else if (phoneNumber.length < 11) {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3, 3)}-${phoneNumber.substr(6)}`;
  } else {
    autoHyphenPhone = `${phoneNumber.substr(0, 3)}-${phoneNumber.substr(3, 4)}-${phoneNumber.substr(7)}`;
  }

  form.value.customerPhoneNumber = autoHyphenPhone;


  // Form validation
  const phoneNumberPattern = /^\d{2,3}-?\d{3,4}-?\d{4}$/;
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  if (!form.value.customerName) {
    window.alert('이름을 입력해주세요.');
    return;
  }
  if (form.value.customerInfoAgreement === 0) {
    window.alert('개인정보제공동의에 동의해주세요.');
    return;
    }
  if (!form.value.customerPhoneNumber) {
    window.alert('전화번호를 입력해주세요.');
    return;
  } else if (!phoneNumberPattern.test(form.value.customerPhoneNumber)) {
    window.alert('전화번호 형식이 올바르지 않습니다.');
    form.value.customerPhoneNumber = '';
    return;
  }
  if (!form.value.customerEmail) {
    window.alert('이메일을 입력해주세요.');
    return;
    } else if (!emailPattern.test(form.value.customerEmail)) {
        window.alert('이메일 형식이 올바르지 않습니다.');
        return;
      }

  try {
    const response = await api.createCustomer(form.value);
    console.log(response);
    window.alert(response.data.content); // 상태 체크 팝업 창
    router.back();
  } catch (error) {
    console.error(error);
    window.alert(error); // 상태 체크 팝업 창
    form.value = JSON.parse(JSON.stringify(initialFormState)); // form 객체를 초기 상태로 재설정
    router.back();
  }
}

async function nationList() {
  try {
    return api.getNations().then(response => {
      isLoading.value = false;
      return response;
    });
  } catch (error) {
    console.error(error);
  }
}

onMounted(async () => {
  countries.value = await nationList();
});
</script>

<template>
  <div class="container-fluid position-relative d-flex p-0">
    <!-- Spinner Start -->
    <div v-if="isLoading" id="spinner"
         class="show bg-dark position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
      <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <!-- Spinner End -->

    <!-- Table Start -->
    <div class="container-fluid pt-4 px-4">
      <div class="bg-secondary rounded-top p-4" style="background: #f7f7f7;">
        <h3 class="mb-4">고객 등록</h3>
        <div class="form-submit">
          <form @submit.prevent="handleSubmit">
            <!-- 등록 버튼 누를 시 고객 데이터 저장 -->
            <button type="submit" class="btn-submit">등록</button>
            <br/><br/>
          </form>
        </div>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <div class="half">
              <label for="korean-name">* 이름:</label>
              <input type="text" id="korean-name" v-model="form.customerName"/>
            </div>

            <div class="half">
              <label for="english-name">* 영문 이름:</label>
              <input type="text" id="english-name" v-model="form.customerEnglishName"/>
            </div>
          </div>
          <div class="form-group">
            <div class="half">
              <label for="phone">* 전화번호:</label>
              <input type="tel" id="phone" v-model="form.customerPhoneNumber" pattern="[0-9]{3}-[0-9]{3,4}-[0-9]{4}" />
            </div>

            <div class="half">
              <label for="customer-type">고객 타입:</label>
              <select id="customer-type" v-model="form.customerType">
                <option value="개인">개인</option>
                <option value="법인">법인</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="third">
              <label for="country">국가:</label>
              <select id="country" v-model="form.nationCodeFk" v-if="countries.length">
                <option v-for="country in countries" :value="country.nationCodePk">
                  {{ country.nationName }}
                </option>
              </select>
            </div>
            <div class="third">
              <label for="gender">성별:</label>
              <select id="gender" v-model="form.customerGender">
                <option value="남성">남성</option>
                <option value="여성">여성</option>
              </select>
            </div>
            <div class="third">
              <div class="form-group checkbox-group">
                <input
                    type="checkbox"
                    id="privacy-consent"
                    v-model="form.customerInfoAgreement"
                    false-value=0
                    true-value=1
                />
                <label for="privacy-consent">개인정보제공동의</label>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label for="email">* 이메일:</label>
            <input type="email" id="email" v-model="form.customerEmail"/>
          </div>
          <div class="form-group">
            <label for="address">주소:</label>
            <input type="text" id="address" v-model="form.customerAddress" style="width: 500px;"/>
          </div>
        </form>
      </div>
    </div>
    <!-- Table End -->

    <!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top">
      <i class="bi bi-arrow-up"></i>
    </a>
  </div>
</template>

<style>
.dropdown-icon {
  transition: transform 0.5s;
}

.filter-container {
  display: none;
  position: absolute;
  top: 50px;
  right: 10px;
  width: 500px;
  padding: 10px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 10px;
}

.filter-container::before {
  content: "";
  position: absolute;
  top: -10px;
  right: 20px;
  border-width: 0 10px 10px 10px;
  border-style: solid;
  border-color: transparent transparent white transparent;
}

.position-relative-container {
  position: relative;
  display: flex;
  justify-content: space-between;
}

.emoji {
  margin-right: 10px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group input,
.form-group select {
  width: 300px;
  padding: 8px 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-group input[type="checkbox"] {
  width: auto;
}

.form-group select {
  width: 100px;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #4c724d;
  outline: none;
}

.form-group .half {
  display: inline-block;
  width: calc(50% - 10px);
}

.form-group .half:first-child {
  margin-right: 10px;
}

.form-group .third {
  display: inline-block;
  width: calc(33% - 10px);
}

.form-group .third:first-child {
  margin-right: 10px;
}

.form-group .checkbox-group label {
  margin: 0 0 0 5px;
}

.btn-submit {
  width: 60px;
  padding: 6px;
  border: none;
  border-radius: 5px;
  background-color: #7f6739;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.btn-submit:hover {
  background-color: #7eb17f;
}

.form-submit {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  height: 40px;
}
</style>
