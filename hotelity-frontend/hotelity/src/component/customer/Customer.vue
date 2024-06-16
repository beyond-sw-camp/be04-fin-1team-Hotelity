<template>
  <div class="container" v-if="customer">
    <div class="title-container">
      <h2>고객 정보</h2>
      <div v-if="!isEditMode">
        <button class="btn btn-primary btn-sm me-2" @click="toggleEditMode">
          <i class="bi bi-pencil-square"></i>
        </button>
        <button class="btn btn-danger btn-sm" @click="confirmDelete">
          <i class="bi bi-trash"></i>
        </button>
      </div>
      <div v-else>
        <button class="btn btn-primary btn-sm me-2" @click="updateCustomerInfo">
          <i class="bi bi-save"></i>
        </button>
        <button class="btn btn-danger btn-sm" @click="toggleEditMode">
          <i class="bi bi-x-square"></i>
        </button>
      </div>
    </div>
    <div v-if="!isEditMode" class="customer-info">
      <div class="field-group">
        <label>고객 코드</label>
        <input type="text" v-model="customer.customerCodePk" readonly/>
      </div>
      <div class="field-group">
        <label>고객 타입</label>
        <input type="text" v-model="customer.customerType" readonly/>
      </div>
      <div class="field-group">
        <label>국가</label>
        <input type="text" v-model="customer.nationName" readonly/>
      </div>
      <div class="field-group">
        <label>이름</label>
        <input type="text" v-model="customer.customerName" readonly/>
      </div>
      <div class="field-group">
        <label>영문 이름</label>
        <input type="text" v-model="customer.customerEnglishName" readonly/>
      </div>
      <div class="field-group">
        <label>이메일</label>
        <input type="email" v-model="customer.customerEmail" readonly/>
      </div>
      <div class="field-group">
        <label>성별</label>
        <input type="text" v-model="customer.customerGender" readonly/>
      </div>
      <div class="field-group">
        <label>주소</label>
        <input type="text" v-model="customer.customerAddress" readonly/>
      </div>
      <div class="field-group">
        <label>전화번호</label>
        <input type="text" v-model="customer.customerPhoneNumber" readonly/>
      </div>
      <div class="field-group">
        <label>회원 멤버십 등급</label>
        <input type="text" v-model="customer.membershipLevelName" readonly/>
      </div>
      <div class="field-group">
        <label>가입일자</label>
        <input type="text" v-model="customer.customerRegisteredDate" readonly/>
      </div>
    </div>

    <div v-else class="customer-info">
      <div class="field-group">
        <label>고객 코드</label>
        <input type="text" v-model="customer.customerCodePk" disabled/>
      </div>
      <div class="field-group">
        <label>고객 타입</label>
        <select id="customer-type" v-model="customer.customerType">
          <option value="개인">개인</option>
          <option value="법인">법인</option>
        </select>
      </div>
      <div class="field-group">
        <label>국가</label>
        <!--        <select id="country" v-model="customer.nationName">-->
        <select id="country" v-model="customer.nationCodeFk">
          <option v-for="country in countries" :value="country.nationCodePk" :key="country.nationCodePk">
            {{ country.nationName }}
          </option>
        </select>
      </div>
      <div class="field-group">
        <label>이름</label>
        <input type="text" v-model="customer.customerName"/>
      </div>
      <div class="field-group">
        <label>영문 이름</label>
        <input type="text" v-model="customer.customerEnglishName"/>
      </div>
      <div class="field-group">
        <label>이메일</label>
        <input type="email" v-model="customer.customerEmail"/>
      </div>
      <div class="field-group">
        <label>성별</label>
        <select id="gender" v-model="customer.customerGender">
          <option value="남성">남성</option>
          <option value="여성">여성</option>
        </select>
      </div>
      <div class="field-group">
        <label>주소</label>
        <input type="text" v-model="customer.customerAddress"/>
      </div>
      <div class="field-group">
        <label>전화번호</label>
        <input type="text" v-model="customer.customerPhoneNumber"/>
      </div>
      <div class="field-group">
        <label>회원 멤버십 등급</label>
        <select id="membership" v-model="customer.membershipLevelName">
          <option v-for="membership in memberships" :value="membership.membershipLevelName"
                  :key="membership.membershipLevelCodePk">
            {{ membership.membershipLevelName }}
          </option>
        </select>
      </div>
      <div class="field-group">
        <label>가입일자</label>
        <input type="text" v-model="customer.customerRegisteredDate" disabled/>
      </div>
    </div>

    <div class="section">
      <h3>결제 내역 <a v-if="customer.payment.length > 0" class="c-pointer" @click="navigateToPayment">+더보기</a></h3>
      <table v-if="customer.payment.length > 0">
        <thead>
        <tr>
          <th>결제 일자</th>
          <th>결제 종류</th>
          <th>결제 수단</th>
          <th>결제 금액</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="customerPayment in customer.payment" :key="customerPayment.paymentCodePk">
          <td>{{ customerPayment.paymentDate }}</td>
          <td>{{ customerPayment.paymentTypeName }}</td>
          <td>{{ customerPayment.paymentMethod }}</td>
          <td>{{ customerPayment.paymentAmount }}</td>
        </tr>
        </tbody>
      </table>
      <p v-else>결제 내역이 없습니다.</p>
    </div>
    <div class="section">
      <h3>등록 VOC <a v-if="customer.voc.length > 0" class="c-pointer" @click="navigateToVoc">+더보기</a></h3>
      <table v-if="customer.voc.length > 0">
        <thead>
        <tr>
          <th>제목</th>
          <th>내용</th>
          <th>처리 상태</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="customerVoc in customer.voc" :key="customerVoc.vocCodePk">
          <td>{{ customerVoc.vocTitle }}</td>
          <td>{{ customerVoc.vocContent }}</td>
          <td>{{ customerVoc.vocProcessStatus === 0 ? "처리 중" : "처리 완료" }}</td>
        </tr>
        </tbody>
      </table>
      <p v-else>등록된 VOC가 없습니다.</p>
    </div>
<!--    <div class="section">-->
<!--      <h3>보유 쿠폰 <a v-if="customer.couponIssue.length > 0">+더보기</a></h3>-->
<!--      <div v-if="customer.couponIssue.length > 0" class="coupons">-->
<!--        <div class="coupon" v-for="customerCoupon in customer.couponIssue" :key="customerCoupon.couponIssueCodePk">-->
<!--          {{ customerCoupon.couponName }}-->
<!--        </div>-->
<!--      </div>-->
<!--      <p v-else>보유 쿠폰이 없습니다.</p>-->
<!--    </div>-->
  </div>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getCustomer, updateCustomer, deleteCustomer, getNations, getMemberships} from "@/api/apiService";

const router = useRouter();
const route = useRoute();
const customer = ref(null);
const customerCodePk = route.params;
const isEditMode = ref(false); // 수정 모드 상태 관리
const countries = ref([]);
const memberships = ref([]);

console.log('customerCodePk', customerCodePk);

const fetchCustomer = async () => {
  try {
    const response = await getCustomer(customerCodePk.id);
    console.log('response', response)
    customer.value = response;
  } catch (error) {
    console.error('Error fetching customer:', error);
  }
};

const confirmDelete = () => {
  console.log('customerCodePk', customerCodePk);

  if (confirm('해당 고객을 정말 삭제하시겠습니까?')) {
    deleteCustomer(customerCodePk.id).then((res) => {

      if (res.resultCode === 200) {
        alert('고객이 성공적으로 삭제되었습니다.');
        router.back();
      } else {
        alert('고객 삭제 중 오류가 발생했습니다.');
      }
    });
  }
}

const updateCustomerInfo = () => {
  if (confirm('고객 정보를 수정하시겠습니까?')) {
    console.log('customer', customer.value);

    memberships.value.forEach((membership) => {
      if (membership.membershipLevelName === customer.value.membershipLevelName) {
        console.log(customer.value);
        customer.value.membershipLevelCode = membership.membershipLevelCodePk;
        console.log(customer.value);
      }
    });

    const updatedCustomerInfo = {
      customerName: customer.value.customerName,
      customerEmail: customer.value.customerEmail,
      customerPhoneNumber: customer.value.customerPhoneNumber,
      customerEnglishName: customer.value.customerEnglishName,
      customerAddress: customer.value.customerAddress,
      customerStatus: customer.value.customerStatus,
      customerType: customer.value.customerType,
      nationCodeFk: customer.value.nationCodeFk,
      customerGender: customer.value.customerGender,
      membershipLevelCode: customer.value.membershipLevelCode
    };


    updateCustomer(customerCodePk.id, updatedCustomerInfo).then((res) => {
      console.log(res);
      if (res.resultCode === 200) {
        alert('고객 정보가 성공적으로 수정되었습니다.');
        isEditMode.value = false;
        fetchCustomer();
      } else {
        alert('고객 정보 수정 중 오류가 발생했습니다.');
      }
    });
  }
}

const toggleEditMode = async () => {
  if (isEditMode.value === true) {
    if (confirm('수정을 취소하시겠습니까?')) {
      isEditMode.value = !isEditMode.value;
    }
  } else {
    isEditMode.value = !isEditMode.value;
  }
};

const navigateToPayment = () => {
  router.push({name: 'PaymentListWithId', params: {customerCodePk: customerCodePk.id}});
};

const navigateToVoc = () => {
  router.push({name: 'VocListWithId', params: {customerCodePk: customerCodePk.id}});
};

onMounted(async () => {
  await fetchCustomer();
  countries.value = await getNations();
  getMemberships({pageNum: 0}).then((res) => {
    memberships.value = res.data.content;
    console.log(res);
  });
});

</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.title-container {
  display: flex;
  justify-content: space-between;
}

.customer-info {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.field-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.field-group label {
  margin-bottom: 5px;
}

.field-group input,
.field-group select,
.field-group button {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.field-group button {
  margin-top: 10px;
}

.section {
  margin: 40px 0;
}

.section h3 {
  display: flex;
  justify-content: space-between;
}

table {
  width: 100%;
  border-collapse: collapse;
}

table th,
table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.coupons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.coupon {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}
</style>