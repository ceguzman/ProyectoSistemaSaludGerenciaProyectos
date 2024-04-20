import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicalProceduresComponent } from './list/medical-procedures.component';
import { MedicalProceduresDetailComponent } from './detail/medical-procedures-detail.component';
import { MedicalProceduresUpdateComponent } from './update/medical-procedures-update.component';
import MedicalProceduresResolve from './route/medical-procedures-routing-resolve.service';

const medicalProceduresRoute: Routes = [
  {
    path: '',
    component: MedicalProceduresComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicalProceduresDetailComponent,
    resolve: {
      medicalProcedures: MedicalProceduresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicalProceduresUpdateComponent,
    resolve: {
      medicalProcedures: MedicalProceduresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicalProceduresUpdateComponent,
    resolve: {
      medicalProcedures: MedicalProceduresResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalProceduresRoute;
