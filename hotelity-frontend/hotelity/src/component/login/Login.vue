<template>
  <div class="container-fluid position-relative d-flex p-0">
    <div class="container-fluid">
      <div class="row h-100 align-items-center justify-content-center" style="min-height: 100vh;">
        <div class="col-12 col-sm-8 col-md-6 col-lg-5 col-xl-4">
          <div class="bg-secondary rounded p-4 p-sm-5 my-4 mx-3" style="background: #f7f7f7;">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <h3 class="text-primary"><i class="fa fa-user-edit me-2"></i>Hotelity</h3>
              <h3>Sign In</h3>
            </div>
            <div class="employee-info-fields">
              <div class="form-floating mb-3 dropdown" data-bs-toggle="dropdown">
                <input type="text" class="form-control" id="floatingBranch" placeholder="HQ" readonly
                       style="background-color: #f8f8f8;">
                <label for="floatingBranch">지점</label>
              </div>
              <div id="dropdownBranch" class="dropdown-menu bg-dark border-0 rounded-0 rounded-bottom m-0">
              </div>
              <div class="form-floating mb-3">
                <input type="number" class="form-control" id="floatingEmployeeCode" placeholder="name@example.com" v-model="employeeCode">
                <label for="floatingEmployeeCode">사번</label>
              </div>
            </div>
            <div class="form-floating mb-4">
              <input type="password" class="form-control" id="floatingPassword" placeholder="Password" v-model="password">
              <label for="floatingPassword">비밀번호</label>
            </div>
            <button id="btnSignIn" type="submit" class="btn btn-primary py-3 w-100 mb-4" @click="signIn">Sign In</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { useAuthStore } from '@/store';
  import { useRouter } from 'vue-router';
  import { ref, onMounted } from 'vue';
  import { getBranches, login } from '@/api/apiService.js';

  const authStore = useAuthStore();
  const router = useRouter();

  const branchCode = ref('');
  const employeeCode = ref('');
  const password = ref('');

  const signIn = () => {
    if (validateInput()) {
      const loginInfo = {
        branchCode: branchCode.value,
        employeeCode: employeeCode.value,
        employPassword: password.value
      };

      login(loginInfo).then((res) => {
        if (res.status === 200) {
          const header = res.headers;
          const accessToken = header['authorization'];

          window.localStorage.setItem('Authorization', accessToken);

          authStore.login({
              branchCode: branchCode.value,
              employeeCode: employeeCode.value,
          });

          document.querySelector(".content").classList.remove('open');

          const redirect = router.currentRoute.value.query.redirect || '/';
          router.replace(redirect);
        } else {
          alert('로그인 실패');
        }
      }).catch((err) => {
        alert('로그인 실패');
      });
    }
  };

  onMounted(() => {
    setBranches();
  });

  const validateInput = () => {
    if (branchCode.value === '') {
      alert('지점을 선택해주세요.');
      return false;
    }

    if (employeeCode.value === '') {
      alert('사번을 입력해주세요.');
      return false
    }

    if (password.value === '') {
      alert('비밀번호를 입력해주세요.');
      return false;
    }

    return true;
  };

  const setBranches = () => {
    getBranches().then((res) => {
      console.log(res);

      // dropdownBranch 하위에 dropdown-item 추가
      const dropdownBranch = document.getElementById('dropdownBranch');
      res.data.forEach((branch) => {
        const dropdownItem = document.createElement('div');
        dropdownItem.classList.add('dropdown-item');
        dropdownItem.textContent = branch.branchName;
        dropdownItem.setAttribute('data-branch-code', branch.branchCodePk);
        dropdownItem.addEventListener('click', dropdownItemClickListener);
        dropdownBranch.appendChild(dropdownItem);
      });
    });
  };

  const dropdownItemClickListener = (e) => {

    // floatingBranch input에 선택한 branchName 적용
    const floatingBranch = document.getElementById('floatingBranch');
    floatingBranch.value = e.target.textContent;
    branchCode.value = e.target.getAttribute('data-branch-code');
  };
</script>

<style>

  .container-fluid {
    background-color: #f7f7f7;
  }

  .employee-info-fields {
    display: flex;
    justify-content: space-between;
    gap: 10px;
  }
</style>
