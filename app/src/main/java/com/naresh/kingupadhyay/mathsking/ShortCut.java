package com.naresh.kingupadhyay.mathsking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ShortCut extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.content_main, container, false);;
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button algebra;
        public Button determinants;
        public Button matrices;
        public Button logarithmic;
        public Button series;
        public Button equation;
        public Button complex;
        public Button pnc;
        public Button binomial;
        public Button probability;
        public Button differential;
        public Button functions;
        public Button limit;
        public Button continuity;
        public Button differentiability;
        public Button differentiation;
        public Button monotonicity;
        public Button max_min;
        public Button tan_normal;
        public Button rate;
        public Button lagrange;
        public Button integral;
        public Button indefinite_inti;
        public Button definite_inti;
        public Button area;
        public Button differen_eqn;
        public Button trigonometry;
        public Button ratio_identity;
        public Button trigo_eq;
        public Button inverse_trigo;
        public Button heigh_dist;
        public Button two_d;
        public Button straight_line;
        public Button pair_straight_line;
        public Button circle;
        public Button parabola;
        public Button ellipse;
        public Button hyperbola;
        public Button three_d;
        public Button vector;
        public RelativeLayout rl_algebra;
        public RelativeLayout rl_differential;
        public RelativeLayout rl_integral;
        public RelativeLayout rl_trigonometry;
        public RelativeLayout rl_twoD;
        public boolean help= true;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_course, parent, false));

            rl_algebra=itemView.findViewById(R.id.child_layout);
            rl_algebra.setVisibility(View.GONE);
            algebra = (Button)itemView.findViewById(R.id.algebra);
            algebra.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            algebra.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(rl_algebra.getVisibility()==View.GONE){
                        rl_algebra.setVisibility(View.VISIBLE);
                        algebra.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
                        algebra.setCompoundDrawablePadding(10);
                        //algebra.setTextColor(Color.BLACK);
                    }else {
                        rl_algebra.setVisibility(View.GONE);
                        algebra.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_action_expand, 0);
                    }

                }
            });
            determinants = (Button)itemView.findViewById(R.id.determinants);
            determinants.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "determinants");
                    context.startActivity(intent);
                }
            });
            matrices= (Button)itemView.findViewById(R.id.matrices);
            matrices.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "matrices");
                    context.startActivity(intent);
                }
            });

            logarithmic = (Button)itemView.findViewById(R.id.logarithmic);
            logarithmic.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "logarithmic");
                    context.startActivity(intent);
                }
            });
            series = (Button)itemView.findViewById(R.id.series);
            series.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "series");
                    context.startActivity(intent);
                }
            });

            equation = (Button)itemView.findViewById(R.id.equation);
            equation.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "equation");
                    context.startActivity(intent);
                }
            });
            complex= (Button)itemView.findViewById(R.id.complex);
            complex.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "complex");
                    context.startActivity(intent);
                }
            });
            pnc = (Button)itemView.findViewById(R.id.pnc);
            pnc.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "pnc");
                    context.startActivity(intent);
                }
            });
            binomial = (Button)itemView.findViewById(R.id.binomial);
            binomial.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "binomial");
                    context.startActivity(intent);
                }
            });

            probability = (Button)itemView.findViewById(R.id.probability);
            probability.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","algebra");
                    intent.putExtra("chapter", "probability");
                    context.startActivity(intent);
                }
            });

            rl_differential=itemView.findViewById(R.id.child_layout2);
            rl_differential.setVisibility(View.GONE);
            differential = (Button)itemView.findViewById(R.id.differential);
            differential.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            differential.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(rl_differential.getVisibility()==View.GONE){
                        rl_differential.setVisibility(View.VISIBLE);
                        differential.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
                        differential.setCompoundDrawablePadding(10);
                        //algebra.setTextColor(Color.BLACK);
                    }else {
                        rl_differential.setVisibility(View.GONE);
                        differential.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_action_expand, 0);
                    }

                }
            });
            functions = (Button)itemView.findViewById(R.id.functions);
            functions.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "functions");
                    context.startActivity(intent);
                }
            });
            limit= (Button)itemView.findViewById(R.id.limit);
            limit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "limit");
                    context.startActivity(intent);
                }
            });
            continuity = (Button)itemView.findViewById(R.id.continuity);
            continuity.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "continuity");
                    context.startActivity(intent);
                }
            });
            differentiability = (Button)itemView.findViewById(R.id.differentiability);
            differentiability.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "differentiability");
                    context.startActivity(intent);
                }
            });
            differentiation = (Button)itemView.findViewById(R.id.differentiation);
            differentiation.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "differentiation");
                    context.startActivity(intent);
                }
            });
            monotonicity= (Button)itemView.findViewById(R.id.monotonicity);
            monotonicity.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "monotonicity");
                    context.startActivity(intent);
                }
            });
            max_min = (Button)itemView.findViewById(R.id.max_min);
            max_min.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "max_min");
                    context.startActivity(intent);
                }
            });
            tan_normal = (Button)itemView.findViewById(R.id.tang_normal);
            tan_normal.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "tan_normal");
                    context.startActivity(intent);
                }
            });

            rate = (Button)itemView.findViewById(R.id.rate);
            rate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "rate");
                    context.startActivity(intent);
                }
            });
            lagrange= (Button)itemView.findViewById(R.id.lagrange);
            lagrange.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","differential");
                    intent.putExtra("chapter", "lagrange");
                    context.startActivity(intent);
                }
            });

            rl_integral=itemView.findViewById(R.id.child_layout3);
            rl_integral.setVisibility(View.GONE);
            integral = (Button)itemView.findViewById(R.id.integral);
            integral.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            integral.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(rl_integral.getVisibility()==View.GONE){
                        rl_integral.setVisibility(View.VISIBLE);
                        integral.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
                        integral.setCompoundDrawablePadding(10);
                        //algebra.setTextColor(Color.BLACK);
                    }else {
                        rl_integral.setVisibility(View.GONE);
                        integral.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_action_expand, 0);
                    }

                }
            });
            indefinite_inti = (Button)itemView.findViewById(R.id.indefinite);
            indefinite_inti.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","integral");
                    intent.putExtra("chapter", "indefinite_inti");
                    context.startActivity(intent);
                }
            });
            definite_inti = (Button)itemView.findViewById(R.id.Definite);
            definite_inti.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","integral");
                    intent.putExtra("chapter", "definite_inti");
                    context.startActivity(intent);
                }
            });
            area = (Button)itemView.findViewById(R.id.area);
            area.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","integral");
                    intent.putExtra("chapter", "area");
                    context.startActivity(intent);
                }
            });
            differen_eqn = (Button)itemView.findViewById(R.id.diff_eqn);
            differen_eqn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","integral");
                    intent.putExtra("chapter", "differen_eqn");
                    context.startActivity(intent);
                }
            });
            rl_trigonometry=itemView.findViewById(R.id.child_layout4);
            rl_trigonometry.setVisibility(View.GONE);
            trigonometry = (Button)itemView.findViewById(R.id.trigonometry);
            trigonometry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            trigonometry.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(rl_trigonometry.getVisibility()==View.GONE){
                        rl_trigonometry.setVisibility(View.VISIBLE);
                        trigonometry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
                        trigonometry.setCompoundDrawablePadding(10);
                        //algebra.setTextColor(Color.BLACK);
                    }else {
                        rl_trigonometry.setVisibility(View.GONE);
                        trigonometry.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_action_expand, 0);
                    }

                }
            });
            ratio_identity = (Button)itemView.findViewById(R.id.ratio_identity);
            ratio_identity.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","trigonometry");
                    intent.putExtra("chapter", "ratio_identity");
                    context.startActivity(intent);
                }
            });
            trigo_eq = (Button)itemView.findViewById(R.id.trigo_eqn);
            trigo_eq.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","trigonometry");
                    intent.putExtra("chapter", "trigo_eq");
                    context.startActivity(intent);
                }
            });
            inverse_trigo = (Button)itemView.findViewById(R.id.inverse_trigo);
            inverse_trigo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","trigonometry");
                    intent.putExtra("chapter", "inverse_trigo");
                    context.startActivity(intent);
                }
            });
            heigh_dist = (Button)itemView.findViewById(R.id.height_dist);
            heigh_dist.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","trigonometry");
                    intent.putExtra("chapter", "heigh_dist");
                    context.startActivity(intent);
                }
            });
            rl_twoD=itemView.findViewById(R.id.child_layout5);
            rl_twoD.setVisibility(View.GONE);
            two_d = (Button)itemView.findViewById(R.id.geometry2d);
            two_d.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
            two_d.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(rl_twoD.getVisibility()==View.GONE){
                        rl_twoD.setVisibility(View.VISIBLE);
                        two_d.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
                        two_d.setCompoundDrawablePadding(10);
                        //algebra.setTextColor(Color.BLACK);
                    }else {
                        rl_twoD.setVisibility(View.GONE);
                        two_d.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_action_expand, 0);
                    }

                }
            });
            straight_line = (Button)itemView.findViewById(R.id.straight_line);
            straight_line.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "straight_line");
                    context.startActivity(intent);
                }
            });
            pair_straight_line = (Button)itemView.findViewById(R.id.pair_line);
            pair_straight_line.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "pair_straight_line");
                    context.startActivity(intent);
                }
            });
            circle = (Button)itemView.findViewById(R.id.circle);
            circle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "circle");
                    context.startActivity(intent);
                }
            });
            parabola = (Button)itemView.findViewById(R.id.parabola);
            parabola.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "parabola");
                    context.startActivity(intent);
                }
            });
            ellipse = (Button)itemView.findViewById(R.id.ellipse);
            ellipse.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "ellipse");
                    context.startActivity(intent);
                }
            });
            hyperbola = (Button)itemView.findViewById(R.id.hyperbola);
            hyperbola.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","two_d");
                    intent.putExtra("chapter", "hyperbola");
                    context.startActivity(intent);
                }
            });


            three_d = (Button)itemView.findViewById(R.id.three_d);
            three_d.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","three_d");
                    intent.putExtra("chapter", "three_d");
                    context.startActivity(intent);
                }
            });
            vector = (Button)itemView.findViewById(R.id.vector);
            vector.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Short_note.class);
                    intent.putExtra("book","vector");
                    intent.putExtra("chapter", "vector");
                    context.startActivity(intent);
                }
            });


        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 1;

        public ContentAdapter(Context context) {
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}

