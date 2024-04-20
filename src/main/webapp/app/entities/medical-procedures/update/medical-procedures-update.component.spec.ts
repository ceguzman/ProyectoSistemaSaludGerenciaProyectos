import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';
import { MedicalAuthorizationService } from 'app/entities/medical-authorization/service/medical-authorization.service';
import { MedicalProceduresService } from '../service/medical-procedures.service';
import { IMedicalProcedures } from '../medical-procedures.model';
import { MedicalProceduresFormService } from './medical-procedures-form.service';

import { MedicalProceduresUpdateComponent } from './medical-procedures-update.component';

describe('MedicalProcedures Management Update Component', () => {
  let comp: MedicalProceduresUpdateComponent;
  let fixture: ComponentFixture<MedicalProceduresUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalProceduresFormService: MedicalProceduresFormService;
  let medicalProceduresService: MedicalProceduresService;
  let medicalAuthorizationService: MedicalAuthorizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MedicalProceduresUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MedicalProceduresUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalProceduresUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalProceduresFormService = TestBed.inject(MedicalProceduresFormService);
    medicalProceduresService = TestBed.inject(MedicalProceduresService);
    medicalAuthorizationService = TestBed.inject(MedicalAuthorizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MedicalAuthorization query and add missing value', () => {
      const medicalProcedures: IMedicalProcedures = { id: 456 };
      const medicalAuthorization: IMedicalAuthorization = { id: 16145 };
      medicalProcedures.medicalAuthorization = medicalAuthorization;

      const medicalAuthorizationCollection: IMedicalAuthorization[] = [{ id: 4950 }];
      jest.spyOn(medicalAuthorizationService, 'query').mockReturnValue(of(new HttpResponse({ body: medicalAuthorizationCollection })));
      const additionalMedicalAuthorizations = [medicalAuthorization];
      const expectedCollection: IMedicalAuthorization[] = [...additionalMedicalAuthorizations, ...medicalAuthorizationCollection];
      jest.spyOn(medicalAuthorizationService, 'addMedicalAuthorizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalProcedures });
      comp.ngOnInit();

      expect(medicalAuthorizationService.query).toHaveBeenCalled();
      expect(medicalAuthorizationService.addMedicalAuthorizationToCollectionIfMissing).toHaveBeenCalledWith(
        medicalAuthorizationCollection,
        ...additionalMedicalAuthorizations.map(expect.objectContaining),
      );
      expect(comp.medicalAuthorizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalProcedures: IMedicalProcedures = { id: 456 };
      const medicalAuthorization: IMedicalAuthorization = { id: 8927 };
      medicalProcedures.medicalAuthorization = medicalAuthorization;

      activatedRoute.data = of({ medicalProcedures });
      comp.ngOnInit();

      expect(comp.medicalAuthorizationsSharedCollection).toContain(medicalAuthorization);
      expect(comp.medicalProcedures).toEqual(medicalProcedures);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalProcedures>>();
      const medicalProcedures = { id: 123 };
      jest.spyOn(medicalProceduresFormService, 'getMedicalProcedures').mockReturnValue(medicalProcedures);
      jest.spyOn(medicalProceduresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalProcedures });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalProcedures }));
      saveSubject.complete();

      // THEN
      expect(medicalProceduresFormService.getMedicalProcedures).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalProceduresService.update).toHaveBeenCalledWith(expect.objectContaining(medicalProcedures));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalProcedures>>();
      const medicalProcedures = { id: 123 };
      jest.spyOn(medicalProceduresFormService, 'getMedicalProcedures').mockReturnValue({ id: null });
      jest.spyOn(medicalProceduresService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalProcedures: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalProcedures }));
      saveSubject.complete();

      // THEN
      expect(medicalProceduresFormService.getMedicalProcedures).toHaveBeenCalled();
      expect(medicalProceduresService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalProcedures>>();
      const medicalProcedures = { id: 123 };
      jest.spyOn(medicalProceduresService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalProcedures });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalProceduresService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicalAuthorization', () => {
      it('Should forward to medicalAuthorizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicalAuthorizationService, 'compareMedicalAuthorization');
        comp.compareMedicalAuthorization(entity, entity2);
        expect(medicalAuthorizationService.compareMedicalAuthorization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
