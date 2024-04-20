import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeDiseasesDetailComponent } from './type-diseases-detail.component';

describe('TypeDiseases Management Detail Component', () => {
  let comp: TypeDiseasesDetailComponent;
  let fixture: ComponentFixture<TypeDiseasesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeDiseasesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeDiseasesDetailComponent,
              resolve: { typeDiseases: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeDiseasesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeDiseasesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeDiseases on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeDiseasesDetailComponent);

      // THEN
      expect(instance.typeDiseases()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
